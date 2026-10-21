package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.subtask.SubtaskResponseDto;
import nl.novi.eindopdracht.dtos.subtask.SubtaskRequestDto;
import nl.novi.eindopdracht.entities.SubtaskEntity;
import org.springframework.stereotype.Component;

@Component
public class SubtaskMapper {

    public SubtaskEntity transferToEntity(SubtaskRequestDto dto) {
        SubtaskEntity entity = new SubtaskEntity();

        entity.setTitle(dto.getTitle());

        return entity;
    }


    public SubtaskResponseDto transferToDto(SubtaskEntity entity) {
        SubtaskResponseDto dto = new SubtaskResponseDto();

        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setSortOrder(entity.getSortOrder());
        dto.setCompleted(entity.isCompleted());

        if (entity.getTask() != null) {
            dto.setTaskId(entity.getTask().getId());
        }
        return dto;

    }


}