package nl.novi.eindopdracht.dtos.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.novi.eindopdracht.dtos.subtask.SubtaskResponseDto;
import nl.novi.eindopdracht.enums.DayPart;
import nl.novi.eindopdracht.enums.ProgressStatus;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class TaskResponseDto {

    private Long id;
    private String title;
    private String description;
    private DayPart dayPart;
    private int sortOrder;
    private Long scheduleId;
    private ProgressStatus status;
    private int percentComplete;
    private List<SubtaskResponseDto> subtasks = new ArrayList<>();

}
