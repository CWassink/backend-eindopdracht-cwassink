package nl.novi.eindopdracht.dtos.schedule;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class ScheduleRequestDto {

    @NotBlank(message = "Naam mag niet leeg zijn.")
    @Size(min = 2, max = 50, message = "Naam moet uit minimaal 2 en maximaal 50 karakters bestaan.")
    private String name;

    @NotNull(message = "Datum is verplicht.")
    private LocalDate date;

    @NotNull(message = "Het schema moet aan een gebruiker toegewezen worden.")
    private Long userId;

}
