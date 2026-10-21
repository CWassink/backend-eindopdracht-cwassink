package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
}
