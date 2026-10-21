package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity,Long> {
}
