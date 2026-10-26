package nl.novi.eindopdracht.controllers;

import nl.novi.eindopdracht.dtos.user.UserResponseDto;
import nl.novi.eindopdracht.mappers.UserMapper;
import nl.novi.eindopdracht.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
    }


@GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication){
        UserResponseDto responseDto = userService.getCurrentUserDto(authentication);

        return ResponseEntity.ok(responseDto);


}


}
