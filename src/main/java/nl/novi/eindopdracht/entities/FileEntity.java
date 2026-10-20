package nl.novi.eindopdracht.entities;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "files")
@NoArgsConstructor
@Getter
@Setter
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

}
