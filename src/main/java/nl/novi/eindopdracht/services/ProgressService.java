package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.entities.SubtaskEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.enums.ProgressStatus;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.exceptions.TaskNotCompletedException;
import nl.novi.eindopdracht.mappers.TaskMapper;
import nl.novi.eindopdracht.repositories.ProgressRepository;
import nl.novi.eindopdracht.repositories.SubtaskRepository;
import nl.novi.eindopdracht.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProgressService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public ProgressService(SubtaskRepository subtaskRepository, TaskRepository taskRepository, TaskMapper taskMapper, ProgressRepository progressRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;

    }

    @Transactional
    public TaskResponseDto checkSubtask(Long subtaskId) {
        SubtaskEntity subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new RecordNotFoundException("Subtaak met id " + subtaskId + " niet gevonden."));

        subtask.setCompleted(true);

        TaskEntity task = subtask.getTask();
        recalculateStatus(task);

        TaskEntity savedTask = taskRepository.save(task);

        return taskMapper.transferToDto(savedTask);
    }

    @Transactional
    public TaskResponseDto uncheckSubtask(Long subtaskId) {
        SubtaskEntity subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new RecordNotFoundException("Subtaak met id " + subtaskId + " niet gevonden."));

        subtask.setCompleted(false);

        TaskEntity task = subtask.getTask();
        recalculateStatus(task);

        TaskEntity savedTask = taskRepository.save(task);

        return taskMapper.transferToDto(savedTask);

    }

    @Transactional
    public TaskResponseDto finishTask(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RecordNotFoundException("Taak met id " + taskId + " niet gevonden."));

        for (SubtaskEntity subtask : task.getSubtasks()) {
            if (!subtask.isCompleted()) {
                throw new TaskNotCompletedException("Taak kan pas klaar zijn als alle subtaken zijn afgevinkt.");

            }
        }
        task.getProgress().setStatus(ProgressStatus.AFGEROND);

        TaskEntity savedTask = taskRepository.save(task);
        return taskMapper.transferToDto(savedTask);
    }


    public void recalculateStatus(TaskEntity task) {
        int completedCount = 0;

        for (SubtaskEntity subtask : task.getSubtasks()) {
            if (subtask.isCompleted()) {
                completedCount++;
            }
        }
        if (completedCount == 0) {
            task.getProgress().setStatus(ProgressStatus.OPEN);
        } else {
            task.getProgress().setStatus(ProgressStatus.BEZIG);
        }
    }

}
