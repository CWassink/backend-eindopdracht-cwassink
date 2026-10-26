package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.subtask.SubtaskRequestDto;
import nl.novi.eindopdracht.dtos.subtask.SubtaskResponseDto;
import nl.novi.eindopdracht.entities.SubtaskEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.ProgressStatus;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.SubtaskMapper;
import nl.novi.eindopdracht.repositories.SubtaskRepository;
import nl.novi.eindopdracht.repositories.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final SubtaskMapper subtaskMapper;
    private final ProgressService progressService;
    private final UserService userService;
    private final AccessService accessService;

    public SubtaskService(SubtaskRepository subtaskRepository, TaskRepository taskRepository, SubtaskMapper subtaskMapper, ProgressService progressService, UserService userService, AccessService accessService) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
        this.subtaskMapper = subtaskMapper;
        this.progressService = progressService;
        this.userService = userService;
        this.accessService = accessService;
    }

    @Transactional
    public SubtaskResponseDto createSubtask(SubtaskRequestDto subtaskRequestDto, Authentication authentication) {
        TaskEntity task = taskRepository.findById(subtaskRequestDto.getTaskId())
                .orElseThrow(() -> new RecordNotFoundException("Taak met id " + subtaskRequestDto.getTaskId() + " niet gevonden."));
        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, task.getSchedule());

        SubtaskEntity subtask = subtaskMapper.transferToEntity(subtaskRequestDto);
        subtask.setTask(task);

        int nextSortOrder = determineNextSortOrder(task.getId());
        subtask.setSortOrder(nextSortOrder);

        task.getSubtasks().add(subtask);
        progressService.recalculateStatus(task);

        return subtaskMapper.transferToDto(subtaskRepository.save(subtask));
    }

    public SubtaskResponseDto getSubtaskById(Long subtaskId, Authentication authentication) {
        SubtaskEntity subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new RecordNotFoundException("Subtaak met id " + subtaskId + " niet gevonden."));

        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, subtask.getTask().getSchedule());

        return subtaskMapper.transferToDto(subtask);
    }

    public SubtaskResponseDto updateSubtask(Long subtaskId, SubtaskRequestDto subtaskRequestDto,Authentication authentication) {
        SubtaskEntity subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new RecordNotFoundException("Subtaak met id " + subtaskId + " niet gevonden."));
        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, subtask.getTask().getSchedule());

        subtask.setTitle(subtaskRequestDto.getTitle());

        SubtaskEntity savedSubtask = subtaskRepository.save(subtask);
        return subtaskMapper.transferToDto(savedSubtask);
    }

    @Transactional
    public void deleteSubtask(Long subtaskId, Authentication authentication) {
        SubtaskEntity subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new RecordNotFoundException("Subtaak met id " + subtaskId + " niet gevonden."));
        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, subtask.getTask().getSchedule());

        TaskEntity task = subtask.getTask();

        if (task.getSubtasks() != null) {
            task.getSubtasks().remove(subtask);
        }
        subtaskRepository.delete(subtask);

        if (task.getProgress() != null && task.getProgress().getStatus() != ProgressStatus.AFGEROND) {
            progressService.recalculateStatus(task);
        }

        taskRepository.save(task);
    }

    private int determineNextSortOrder(Long taskId) {
        Optional<SubtaskEntity> highestSubtaskOpt = subtaskRepository.findTopByTaskIdOrderBySortOrderDesc(taskId);

        int nextSortOrder;
        if (highestSubtaskOpt.isPresent()) {
            SubtaskEntity highestSubtask = highestSubtaskOpt.get();
            nextSortOrder = highestSubtask.getSortOrder() + 1;
        } else {
            nextSortOrder = 1;
        }

        return nextSortOrder;
    }
}
