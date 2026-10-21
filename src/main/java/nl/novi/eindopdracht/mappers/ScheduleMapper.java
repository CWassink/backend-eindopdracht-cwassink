package nl.novi.eindopdracht.mappers;

import org.springframework.stereotype.Component;
import nl.novi.eindopdracht.dtos.schedule.ScheduleRequestDto;
import nl.novi.eindopdracht.dtos.schedule.ScheduleResponseDto;
import nl.novi.eindopdracht.entities.ScheduleEntity;

@Component
public class ScheduleMapper {

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



        return dto;
    }
}