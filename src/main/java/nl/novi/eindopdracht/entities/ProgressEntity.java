package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.novi.eindopdracht.enums.ProgressStatus;

@Entity
@Table(name = "progress_reports")
@NoArgsConstructor
@Getter
@Setter
public class ProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgressStatus status = ProgressStatus.OPEN;

    @OneToOne(mappedBy = "progress")
    private TaskEntity task;
}
