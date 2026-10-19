package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "progress_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int percentage = 0;

    @Column(nullable = false)
    private String status = "gestart";
    private String note;

    @OneToOne(mappedBy = "progress")
    private TaskEntity task;
}
