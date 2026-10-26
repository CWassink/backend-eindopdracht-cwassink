package nl.novi.eindopdracht.repositories;

import nl.novi.eindopdracht.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);


}
