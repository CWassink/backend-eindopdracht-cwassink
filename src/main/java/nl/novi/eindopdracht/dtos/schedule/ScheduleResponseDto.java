package nl.novi.eindopdracht.dtos.schedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.novi.eindopdracht.dtos.task.TaskResponseDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class ScheduleResponseDto {

    private Long id;
    private String userName;
    private String name;
    private LocalDate date;
    private Long userId;
    private List<TaskResponseDto> tasks = new ArrayList<>();

}
