package nl.novi.eindopdracht.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequestDto {

    @NotBlank(message = "Username mag niet leeg zijn.")
    @Size(min = 2, max = 100, message = "De username moet uit minimaal 2 en maximaal 100 karakters bestaan")
    private String username;

    @NotBlank (message = "Wachtwoord mag niet leeg zijn")
    private String password;

    private String name;

}
