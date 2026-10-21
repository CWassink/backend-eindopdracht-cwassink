package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.SubtaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubtaskRepository extends JpaRepository<SubtaskEntity,Long> {
}
