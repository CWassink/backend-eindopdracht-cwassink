package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.entities.TaskEntity;
import org.springframework.stereotype.Component;
import nl.novi.eindopdracht.dtos.schedule.ScheduleRequestDto;
import nl.novi.eindopdracht.dtos.schedule.ScheduleResponseDto;
import nl.novi.eindopdracht.entities.ScheduleEntity;

import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduleMapper {

    private final TaskMapper taskMapper;

    public ScheduleMapper(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    public ScheduleEntity transferToEntity(ScheduleRequestDto dto) {
        ScheduleEntity entity = new ScheduleEntity();

        entity.setName(dto.getName());
        entity.setDate(dto.getDate());

        return entity;
    }

    public ScheduleResponseDto transferToDto(ScheduleEntity entity) {
        ScheduleResponseDto dto = new ScheduleResponseDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
            dto.setUserName(entity.getUser().getName());
        }

        if (entity.getTasks() != null) {
            List<TaskResponseDto> taskDtos = new ArrayList<>();
            for (TaskEntity taskEntity : entity.getTasks()) {
                TaskResponseDto taskDto = taskMapper.transferToDto(taskEntity);
                taskDtos.add(taskDto);
            }
            dto.setTasks(taskDtos);

        }
        return dto;
    }
}