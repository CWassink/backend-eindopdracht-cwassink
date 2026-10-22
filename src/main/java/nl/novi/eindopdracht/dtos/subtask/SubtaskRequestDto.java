package nl.novi.eindopdracht.dtos.subtask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubtaskRequestDto {

    @NotNull(message = "Kies bij welke taak deze subtaak hoort.")
    private Long taskId;

    @NotBlank(message = "Subtaak moet een naam krijgen.")
    @Size(min = 2, max = 100, message = "Minimaal 2 en maximaal 100 karakters.")
    private String title;


}
