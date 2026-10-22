package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.novi.eindopdracht.enums.DayPart;
import org.springframework.core.annotation.Order;


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

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DayPart dayPart;

    @Column(nullable = false)
    private int sortOrder;

    @ManyToOne
    @JoinColumn( name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "progress_id")
    private ProgressEntity progress;

    @OrderBy("sortOrder")
    @OneToMany (mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubtaskEntity> subtasks = new ArrayList<>();

    @OneToOne (mappedBy = "task", cascade = CascadeType.ALL)
    private FileEntity pictogram;



}
