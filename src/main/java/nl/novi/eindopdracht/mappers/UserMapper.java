package nl.novi.eindopdracht.mappers;


import nl.novi.eindopdracht.dtos.user.UserResponseDto;
import nl.novi.eindopdracht.entities.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto transferToDto(UserEntity userEntity) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(userEntity.getId());
        userResponseDto.setUsername(userEntity.getUsername());
        userResponseDto.setName(userEntity.getName());
        userResponseDto.setRole(userEntity.getRole());

        return userResponseDto;

    }
}
