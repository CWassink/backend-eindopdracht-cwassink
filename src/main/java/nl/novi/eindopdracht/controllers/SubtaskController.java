package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.subtask.SubtaskRequestDto;
import nl.novi.eindopdracht.dtos.subtask.SubtaskResponseDto;
import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.services.ProgressService;
import nl.novi.eindopdracht.services.SubtaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/subtasks")
public class SubtaskController {

    private final SubtaskService subtaskService;
    private final ProgressService progressService;

    public SubtaskController(SubtaskService subtaskService, ProgressService progressService) {
        this.subtaskService = subtaskService;
        this.progressService = progressService;
    }

    @PostMapping
    public ResponseEntity<SubtaskResponseDto> createSubtask(@Valid @RequestBody SubtaskRequestDto subtaskRequestDto) {
       SubtaskResponseDto newSubtask = subtaskService.createSubtask(subtaskRequestDto);
       return ResponseEntity.status(HttpStatus.CREATED).body(newSubtask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubtaskResponseDto> getSubtask(@PathVariable Long id) {
        SubtaskResponseDto subtask = subtaskService.getSubtaskById(id);

        return ResponseEntity.ok(subtask);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SubtaskResponseDto>  updateSubtask(@PathVariable Long id,@Valid @RequestBody SubtaskRequestDto subtaskRequestDto) {
        SubtaskResponseDto updateSubtask = subtaskService.updateSubtask(id, subtaskRequestDto);

        return ResponseEntity.ok(updateSubtask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubtask(@PathVariable Long id) {
        subtaskService.deleteSubtask(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/check")
    public ResponseEntity<TaskResponseDto> checkSubtask(@PathVariable Long id) {
        TaskResponseDto task = progressService.checkSubtask(id);

        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}/uncheck")
    public ResponseEntity<TaskResponseDto> uncheckSubtask(@PathVariable Long id) {
        TaskResponseDto task = progressService.uncheckSubtask(id);

        return ResponseEntity.ok(task);
    }





}
