package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import nl.novi.eindopdracht.enums.Role;

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
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;



    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn( name = "caregiver_id")
    private UserEntity caregiver;

    @OneToMany (mappedBy = "user")
    private List<ScheduleEntity> schedules = new ArrayList<>();

    @OneToMany (mappedBy = "caregiver")
    private List<UserEntity> clients = new ArrayList<>();


}
