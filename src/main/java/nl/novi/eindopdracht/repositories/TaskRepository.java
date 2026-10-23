package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.enums.DayPart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {

    Optional<TaskEntity> findTopByScheduleIdAndDayPartOrderBySortOrderDesc(Long scheduleId, DayPart dayPart);

    Long id(Long id);

    Long schedule_Id(Long scheduleId);
}
