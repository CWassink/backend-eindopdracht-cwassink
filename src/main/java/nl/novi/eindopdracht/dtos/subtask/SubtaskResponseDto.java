package nl.novi.eindopdracht.dtos.subtask;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SubtaskResponseDto {

    private Long id;
    private String title;
    private boolean completed;
    private int sortOrder;
    private Long taskId;


}