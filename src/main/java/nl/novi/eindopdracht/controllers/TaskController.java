package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.task.TaskRequestDto;
import nl.novi.eindopdracht.dtos.task.TaskResponseDto;
import nl.novi.eindopdracht.entities.FileEntity;
import nl.novi.eindopdracht.services.FileService;
import nl.novi.eindopdracht.services.ProgressService;
import nl.novi.eindopdracht.services.TaskService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ProgressService progressService;
    private final FileService fileService;


    public TaskController(TaskService taskService, ProgressService progressService, FileService fileService) {
        this.taskService = taskService;
        this.progressService = progressService;
        this.fileService = fileService;
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@Valid @RequestBody TaskRequestDto taskRequestDto, Authentication authentication) {
        TaskResponseDto newTask = taskService.createTask(taskRequestDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getTask(@PathVariable Long id, Authentication authentication) {
        TaskResponseDto task = taskService.getTaskById(id, authentication);

        return ResponseEntity.ok(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDto taskRequestDto, Authentication authentication) {
        TaskResponseDto updateTask = taskService.updateTask(id, taskRequestDto, authentication);

        return ResponseEntity.ok(updateTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, Authentication authentication) {
        taskService.deleteTask(id, authentication);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<TaskResponseDto> finishTask(@PathVariable Long id, Authentication authentication) {
        TaskResponseDto task = progressService.finishTask(id, authentication);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/{id}/pictogram")
    public ResponseEntity<Void> uploadPictogram(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) throws IOException {

        fileService.uploadPictogram(id, file, authentication);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pictogram")
    public ResponseEntity<byte[]> getPictogram(
            @PathVariable Long id,
            Authentication authentication) {

        FileEntity fileEntity = fileService.getPictogram(id, authentication);
        MediaType mediaType = MediaType.parseMediaType(fileEntity.getContentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileEntity.getFileName())
                .body(fileEntity.getContents());
    }
}
