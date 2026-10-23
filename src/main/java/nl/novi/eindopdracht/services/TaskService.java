package nl.novi.eindopdracht.services;


import nl.novi.eindopdracht.dtos.task.TaskRequestDto;
import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.entities.ProgressEntity;
import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.enums.DayPart;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.TaskMapper;
import nl.novi.eindopdracht.repositories.ScheduleRepository;
import nl.novi.eindopdracht.repositories.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ScheduleRepository scheduleRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, ScheduleRepository scheduleRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.scheduleRepository = scheduleRepository;
        this.taskMapper = taskMapper;
    }

    public TaskResponseDto createTask(TaskRequestDto taskRequestDto) {
        ScheduleEntity schedule = scheduleRepository.findById(taskRequestDto.getScheduleId())
                .orElseThrow(() -> new RecordNotFoundException("Schema met id " + taskRequestDto.getScheduleId() + " niet gevonden."));

        TaskEntity task = taskMapper.transferToEntity(taskRequestDto);
        task.setSchedule(schedule);

        int nextSortOrder = determineNextSortOrder(schedule.getId(), task.getDayPart());
        task.setSortOrder(nextSortOrder);


        ProgressEntity progressEntity = new ProgressEntity();
        task.setProgress(progressEntity);
        progressEntity.setTask(task);

        TaskEntity savedTask = taskRepository.save(task);

        return taskMapper.transferToDto(savedTask);

    }

    public TaskResponseDto getTaskById(Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RecordNotFoundException("Taak " + taskId + " niet gevonden."));
        return taskMapper.transferToDto(task);
    }

    public TaskResponseDto updateTask(Long taskId, TaskRequestDto taskRequestDto) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RecordNotFoundException("Taak met id " + taskId + " niet gevonden."));
        DayPart oldDayPart = task.getDayPart();

        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setDayPart(taskRequestDto.getDayPart());

        if (task.getDayPart() != oldDayPart) {
            int newSortOrder = determineNextSortOrder(task.getSchedule().getId(), task.getDayPart());
            task.setSortOrder(newSortOrder);
        }

        TaskEntity savedTask = taskRepository.save(task);
        return taskMapper.transferToDto(savedTask);
    }

    public void deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new RecordNotFoundException("Taak " + taskId + " niet gevonden.");
        }
        taskRepository.deleteById(taskId);
    }

    private int determineNextSortOrder(Long scheduleId, DayPart dayPart) {
        Optional<TaskEntity> highestTaskOpt = taskRepository.findTopByScheduleIdAndDayPartOrderBySortOrderDesc(
                scheduleId,
                dayPart
        );
        int nextSortOrder;
        if (highestTaskOpt.isPresent()) {
            TaskEntity highestTask = highestTaskOpt.get();
            nextSortOrder = highestTask.getSortOrder() + 1;
        } else {
            nextSortOrder = 1;
        }

        return nextSortOrder;
    }

}
