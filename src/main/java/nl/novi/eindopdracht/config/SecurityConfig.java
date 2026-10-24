package nl.novi.eindopdracht.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.resourceserver.jwt.audiences}")
    private String audience;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(hp -> hp.disable())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {
                })
                .oauth2ResourceServer(oath2 -> oath2.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()).decoder(jwtDecoder())))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/schedules").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.GET, "/schedules").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.PUT, "/schedules/{id}").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.GET, "/schedules/{id}").hasAnyAuthority("CAREGIVER","CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/schedules/{id}").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.POST, "/tasks","/subtasks").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.GET, "/tasks/{id}","/subtasks/{id}").hasAnyAuthority("CAREGIVER","CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/tasks/{id}","/subtasks/{id}").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.DELETE, "/tasks/{id}","/subtasks/{id}").hasAuthority("CAREGIVER")
                        .requestMatchers(HttpMethod.PATCH, "/subtasks/{id}/check","/subtasks/{id}/uncheck").hasAnyAuthority("CAREGIVER","CLIENT")
                        .requestMatchers(HttpMethod.PATCH, "/tasks/{id}/finish").hasAnyAuthority("CAREGIVER","CLIENT")
                        .anyRequest().denyAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();


    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);

        OAuth2TokenValidator<Jwt> audienceValidator = new JwtAudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuerUri = JwtValidators.createDefaultWithIssuer(issuerUri);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuerUri, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);
        return jwtDecoder;

    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt source) {
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (String authority : getAuthorities(source)) {
                    grantedAuthorities.add(new SimpleGrantedAuthority( authority));
                }
                return grantedAuthorities;
            }
            private List<String> getAuthorities(Jwt jwt){
                Map<String, Object> resourceAcces = jwt.getClaim("resource_access");
                if(resourceAcces != null){
                    if( resourceAcces.get(clientId) instanceof Map) {
                        Map<String, Object> client = (Map<String, Object>) resourceAcces.get(clientId);
                        if(client != null && client.containsKey("roles")) {
                            return (List<String>) client.get("roles");
                        }
                    }
                }
                return new ArrayList<>();
            }
        });
        return jwtAuthenticationConverter;
    }

}

