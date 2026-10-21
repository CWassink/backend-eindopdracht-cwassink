package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.ProgressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<ProgressEntity,Long> {
}
