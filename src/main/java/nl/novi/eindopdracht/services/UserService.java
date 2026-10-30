package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.user.ClientRequestDto;
import nl.novi.eindopdracht.dtos.user.UserResponseDto;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.Role;
import nl.novi.eindopdracht.exceptions.ClientAlreadyAssignedException;
import nl.novi.eindopdracht.exceptions.NoAccessException;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.UserMapper;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KeycloakAdminService keycloakAdminService;

    public UserService(UserRepository userRepository, UserMapper userMapper, KeycloakAdminService keycloakAdminService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.keycloakAdminService = keycloakAdminService;
    }

    public UserEntity getCurrentUser(Authentication authentication) {
        String username = authentication.getName();

        return userRepository.findByUsername(username)
                .orElseGet(() -> {
                    UserEntity newUser = new UserEntity();
                    newUser.setUsername(username);
                    newUser.setName(username);

                    Role role = Role.CAREGIVER;
                    for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                        if (grantedAuthority.getAuthority().equals("CLIENT")) {
                            role = Role.CLIENT;
                            break;
                        }
                    }
                    newUser.setRole(role);

                    return userRepository.save(newUser);
                });
    }

    public UserResponseDto getCurrentUserDto(Authentication authentication) {
        UserEntity userEntity = getCurrentUser(authentication);
        return userMapper.transferToDto(userEntity);
    }

    public UserResponseDto linkClient(String username, Authentication authentication) {
        UserEntity caregiver = getCurrentUser(authentication);
        UserEntity client = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("Cliënt met gebruikersnaam " + username + " niet gevonden."));

        if (!Role.CLIENT.equals(client.getRole())) {
            throw new NoAccessException("Toegang geweigerd: De geselecteerde gebruiker is geen cliënt.");
        }

        if (client.getCaregiver() != null) {
            throw new ClientAlreadyAssignedException("Conflict: Deze cliënt is al gekoppeld aan een andere zorgverlener.");
        }
        client.setCaregiver(caregiver);
        UserEntity savedClient = userRepository.save(client);

        return userMapper.transferToDto(savedClient);
    }

    public UserResponseDto registerClient(ClientRequestDto request, Authentication authentication) {

        UserEntity currentCaregiver = getCurrentUser(authentication);

        String username = request.getUsername().toLowerCase();

        String keycloakId = keycloakAdminService.registerNewUser(
                username,
                request.getName(),
                request.getPassword()
        );


        UserEntity newClient = new UserEntity();
        newClient.setKeycloakId(keycloakId);
        newClient.setUsername(username);
        newClient.setName(request.getName());
        newClient.setRole(Role.CLIENT);
        newClient.setCaregiver(currentCaregiver);


        UserEntity savedClient = userRepository.save(newClient);
        return userMapper.transferToDto(savedClient);
    }
}