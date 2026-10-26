package nl.novi.eindopdracht.services;


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

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
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


    }

