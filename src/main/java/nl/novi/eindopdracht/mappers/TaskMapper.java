package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.subtask.SubtaskResponseDto;
import nl.novi.eindopdracht.dtos.task.TaskRequestDto;
import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.entities.SubtaskEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.enums.ProgressStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TaskMapper {

    private final SubtaskMapper subtaskMapper;

    public TaskMapper(SubtaskMapper subtaskMapper) {
        this.subtaskMapper = subtaskMapper;
    }

    public TaskEntity transferToEntity(TaskRequestDto dto) {
        TaskEntity entity = new TaskEntity();

        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setDayPart(dto.getDayPart());

        return entity;
    }


    public TaskResponseDto transferToDto(TaskEntity entity) {
        TaskResponseDto dto = new TaskResponseDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setDayPart(entity.getDayPart());
        dto.setSortOrder(entity.getSortOrder());

        if (entity.getSchedule() != null) {
            dto.setScheduleId(entity.getSchedule().getId());
        }

        if (entity.getProgress() != null) {
            dto.setStatus(entity.getProgress().getStatus());
        }
        if (entity.getSubtasks() != null) {
            List<SubtaskResponseDto> subtaskDtos = new ArrayList<>();
            for (SubtaskEntity subtaskEntity : entity.getSubtasks()) {
                SubtaskResponseDto subtaskDto = subtaskMapper.transferToDto(subtaskEntity);
                subtaskDtos.add(subtaskDto);
            }
            dto.setSubtasks(subtaskDtos);
        }

        dto.setPercentComplete(calculatePercentComplete(entity, dto.getStatus()));

        return dto;
    }
    private int calculatePercentComplete(TaskEntity entity, ProgressStatus status) {
        int totalSubtasks = entity.getSubtasks() != null ? entity.getSubtasks().size() : 0;
        int completedSubtasks = 0;

        if (entity.getSubtasks() != null) {
            for (SubtaskEntity subtask : entity.getSubtasks()) {
                if (subtask.isCompleted()) {
                    completedSubtasks++;
                }
            }
        }

        int percentComplete = 0;
        if (totalSubtasks > 0) {
            percentComplete = (completedSubtasks * 100) / totalSubtasks;
        } else {
            if (status == ProgressStatus.AFGEROND) {
                percentComplete = 100;
            }
        }

        return percentComplete;
    }
}