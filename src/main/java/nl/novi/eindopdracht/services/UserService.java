package nl.novi.eindopdracht.services;


import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.Role;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;



@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
                        if (grantedAuthority.getAuthority().equals("CLIENT")){
                            role = Role.CLIENT;
                            break;
                        }
                    }
                     newUser.setRole(role);

                    return userRepository.save(newUser);
                });


    }
}
