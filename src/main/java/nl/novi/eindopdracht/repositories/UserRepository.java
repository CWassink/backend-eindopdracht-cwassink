package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

}
