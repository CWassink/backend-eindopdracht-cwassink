package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "subtasks")
@NoArgsConstructor
@Getter
@Setter
public class SubtaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isCompleted = false;

    @Column(nullable = false)
    private int sortOrder;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;


}
