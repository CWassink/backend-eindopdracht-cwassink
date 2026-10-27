package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.entities.FileEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.exceptions.InvalidFileException;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.repositories.TaskRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final AccessService accessService;

    public FileService(TaskRepository taskRepository, UserService userService, AccessService accessService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.accessService = accessService;
    }


    @Transactional
    public void uploadPictogram(Long taskId, MultipartFile file, Authentication authentication) throws IOException {


        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Bestand mag niet leeg zijn.");
        }


        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/png") && !contentType.equals("image/jpeg"))) {
            throw new InvalidFileException("Alleen PNG of JPG bestanden zijn toegestaan.");
        }


        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RecordNotFoundException("Taak met id " + taskId + " niet gevonden."));


        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, task.getSchedule());


        FileEntity fileEntity = task.getPictogram();

        if (fileEntity == null) {

            fileEntity = new FileEntity();
            fileEntity.setTask(task);
            task.setPictogram(fileEntity);
        }


        fileEntity.setFileName(file.getOriginalFilename());
        fileEntity.setContentType(contentType);
        fileEntity.setContents(file.getBytes());


        taskRepository.save(task);
    }

    @Transactional
    public FileEntity getPictogram(Long taskId, Authentication authentication) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(()-> new RecordNotFoundException("Taak met id " + taskId + " niet gevonden."));

        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, task.getSchedule());

        FileEntity fileEntity = task.getPictogram();
        if (fileEntity == null) {
            throw new RecordNotFoundException("Taak met id " + taskId + " heeft geen pictogram.");
        }
        return fileEntity;
    }
}