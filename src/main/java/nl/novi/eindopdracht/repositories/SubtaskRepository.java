package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.SubtaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubtaskRepository extends JpaRepository<SubtaskEntity,Long> {

    Optional<SubtaskEntity> findTopByTaskIdOrderBySortOrderDesc(Long taskId);
}
