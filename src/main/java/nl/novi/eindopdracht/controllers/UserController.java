package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.user.ClientRequestDto;
import nl.novi.eindopdracht.dtos.user.UserResponseDto;
import nl.novi.eindopdracht.mappers.UserMapper;
import nl.novi.eindopdracht.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


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

@PostMapping("/clients")
    public ResponseEntity<UserResponseDto> linkClient(@Valid @RequestBody ClientRequestDto clientRequestDto, Authentication authentication){
        UserResponseDto responseDto = userService.linkClient(clientRequestDto.getUsername(),authentication);

        return ResponseEntity.ok(responseDto);
    }

@PostMapping("/clients/new")
    public ResponseEntity<UserResponseDto> registerClient(
            @Valid @RequestBody ClientRequestDto request,
            Authentication authentication) {

        UserResponseDto response = userService.registerClient(request, authentication);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
