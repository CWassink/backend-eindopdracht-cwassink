package nl.novi.eindopdracht.services;


import nl.novi.eindopdracht.dtos.user.UserResponseDto;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.Role;
import nl.novi.eindopdracht.mappers.UserMapper;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = new UserMapper();
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

    @Transactional(readOnly = true)
    public UserResponseDto getCurrentUserDto(Authentication authentication) {
        UserEntity userEntity = getCurrentUser(authentication);

        return userMapper.transferToDto(userEntity);

    }

}

