package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "subtasks")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class SubtaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private boolean isCompleted = false;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;


}
