package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn( name - "creator_id", nullable = false)
    private UserEntity creator;

    @ManyToOne
    @JoinColumn( name - "assignee_id")
    private UserEntity assignee;

    @ManyToOne
    @JoinColumn( name - "schedule_id")
    private ScheduleEntity schedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "progress_id")
    private ProgressEntity progress;

    @OneToMany (mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubtaskEntity> subtasks;

    @OneToMany (mappedBy = "task", cascade = CascadeType.ALL)
    private List<File> files;






}
