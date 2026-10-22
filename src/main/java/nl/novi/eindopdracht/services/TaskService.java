package nl.novi.eindopdracht.services;


import nl.novi.eindopdracht.dtos.task.TaskRequestDto;
import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.TaskMapper;
import nl.novi.eindopdracht.repositories.ScheduleRepository;
import nl.novi.eindopdracht.repositories.TaskRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

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

        TaskEntity savedTask = taskRepository.save(task);

        return taskMapper.transferToDto(savedTask);

    }

}
