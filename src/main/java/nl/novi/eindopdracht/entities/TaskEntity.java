package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;

    @ManyToOne
    @JoinColumn( name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "progress_id")
    private ProgressEntity progress;

    @OneToMany (mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubtaskEntity> subtasks = new ArrayList<>();

    @OneToMany (mappedBy = "task", cascade = CascadeType.ALL)
    private List<FileEntity> files = new ArrayList<>();






}
