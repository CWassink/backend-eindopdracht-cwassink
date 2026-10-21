package nl.novi.eindopdracht.dtos.task;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.novi.eindopdracht.enums.DayPart;

@NoArgsConstructor
@Getter
@Setter
public class TaskRequestDto {

    @NotBlank(message = "Taak moet een naam krijgen.")
    @Size(min = 2, max = 100, message = "Minimaal 2 en maximaal 100 karakters.")
    private String title;

    @Size(max = 500, message = "Maximaal 500 karakters.")
    private String description;

    @NotNull(message = "Dagdeel is verplicht. kies uit; OCHTEND, MIDDAG, AVOND.")
    private DayPart dayPart;

    @NotNull(message = "Kies bij welk schema de taak hoort.")
    private Long scheduleId;
}
