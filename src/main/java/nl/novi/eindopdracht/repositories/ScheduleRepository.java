package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity,Long> {
    List<ScheduleEntity> findByUserCaregiverId(Long caregiverId);
}
