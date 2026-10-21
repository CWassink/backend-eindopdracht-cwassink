package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity,Long> {
}
