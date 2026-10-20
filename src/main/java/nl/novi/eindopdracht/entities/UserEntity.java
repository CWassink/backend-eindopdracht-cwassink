package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.management.relation.Role;
import java.util.List;

@Entity
@Table (name = "users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    //als Role een Enum mag worden bovenstaande @ManyToOne verwijderen en vervangen door onderstaande code
    //@Enumerated (EnumType.STRING)
    //@cColumn(nullable = false)
    //private Role role;


    @OneToMany (mappedBy = "user")
    private List<ScheduleEntity> schedules;


}
