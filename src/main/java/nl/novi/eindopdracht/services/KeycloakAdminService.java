package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.exceptions.KeycloakException;
import nl.novi.eindopdracht.exceptions.UsernameAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;


@Service
public class KeycloakAdminService {

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    private final RestClient restClient = RestClient.create();

    public String registerNewUser(String username, String name, String password) {
        String adminToken = getAdminToken();
        String userId = createKeycloakUser(username, name, password, adminToken);
        assignClientRole(userId, adminToken);
        return userId;


    }
    private String getAdminToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "client_credentials");
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        try {
            Map<String, Object> response = restClient.post()
                    .uri(serverUrl + "/realms/" + realm + "/protocol/openid-connect/token")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (response == null || !response.containsKey("access_token")) {
                throw new KeycloakException("Geen access token ontvangen van Keycloak.");
            }
            return (String) response.get("access_token");

        } catch (RestClientException e) {
            throw new KeycloakException("Kon geen access token ophalen uit Keycloak.");
        }
    }

    private String createKeycloakUser(String username,String name, String password, String adminToken) {
        Map<String, Object> userBody = Map.of(
                "username", username,
                "firstName", name,
                "enabled", true,
                "credentials", List.of(Map.of(
                        "type", "password",
                        "value", password,
                        "temporary", false
                ))
        );

        String url = serverUrl + "/admin/realms/" + realm + "/users";

        try{
        ResponseEntity<Void> response = restClient.post()
                .uri(url)
                .header("Authorization", "Bearer " + adminToken)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userBody)
                .retrieve()
                .toBodilessEntity();
            String path = response.getHeaders().getLocation().getPath();
            return path.substring(path.lastIndexOf("/") + 1);

        } catch (HttpClientErrorException.Conflict e) {
            throw new UsernameAlreadyExistsException("Gebruikersnaam '" + username + "' bestaat al in Keycloak.");

        } catch (RestClientException e) {
           throw new KeycloakException("Fout bij het communiceren met Keycloak tijdens aanmaken gebruiker: " + e.getMessage());
        }
    }

    private void assignClientRole(String userId, String adminToken) {

        try {
            String urlA = serverUrl + "/admin/realms/" + realm + "/clients?clientId=" + clientId;

            List<Map<String, Object>> clientsList = restClient.get()
                    .uri(urlA)
                    .header("Authorization", "Bearer " + adminToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    });

            if (clientsList == null || clientsList.isEmpty()) {
                throw new KeycloakException("Client '" + clientId + "' kon niet gevonden worden in Keycloak.");
            }

            String internalClientId = (String) clientsList.get(0).get("id");
            String urlB = serverUrl + "/admin/realms/" + realm + "/clients/" + internalClientId + "/roles/CLIENT";

            Map<String, Object> clientRole = restClient.get()
                    .uri(urlB)
                    .header("Authorization", "Bearer " + adminToken)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {
                    });

            if (clientRole == null) {
                throw new KeycloakException("De rol 'CLIENT' kon niet worden opgehaald uit Keycloak.");
            }

            String urlC = serverUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/clients/" + internalClientId;

            restClient.post()
                    .uri(urlC)
                    .header("Authorization", "Bearer " + adminToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(List.of(clientRole))
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientException e) {
            throw new KeycloakException("Fout bij het toewijzen van de CLIENT rol in Keycloak: " + e.getMessage());
        }
    }

}
