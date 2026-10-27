package nl.novi.eindopdracht.services;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.SubtaskEntity;
import nl.novi.eindopdracht.entities.TaskEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.DayPart;
import nl.novi.eindopdracht.exceptions.PdfGenerationException;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.repositories.ScheduleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Service
public class PdfService {

    private final UserService userService;
    private final AccessService accessService;
    private final ScheduleRepository scheduleRepository;

    public PdfService(UserService userService, AccessService accessService, ScheduleRepository scheduleRepository) {
        this.userService = userService;
        this.accessService = accessService;
        this.scheduleRepository = scheduleRepository;
    }

    @Transactional
    public byte[] createSchedulePdf(Long scheduleId, Authentication authentication) {
        ScheduleEntity schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RecordNotFoundException("Schema met id " + scheduleId + " niet gevonden."));

        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, schedule);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            Font titleFont   = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headingFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font normalFont  = FontFactory.getFont(FontFactory.HELVETICA, 12);
            document.open();

            document.add(new Paragraph("Dagschema: " + schedule.getName(), titleFont));

            if (schedule.getDate() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.of("nl"));
                String formattedDate = schedule.getDate().format(formatter);
                document.add(new Paragraph("Datum: " + formattedDate,normalFont));
            }

            document.add(new Paragraph(" "));

            for (DayPart dayPart : DayPart.values()) {
                document.add(new Paragraph( dayPart.name(), headingFont));

               if (schedule.getTasks() != null) {
                    for (TaskEntity task : schedule.getTasks()) {
                        if (task.getDayPart() == dayPart) {

                            String status;
                            if (task.getProgress() != null) {
                                status = task.getProgress().getStatus().name();
                            } else {
                                status = "ONBEKEND";
                            }
                            Paragraph taskParagraph = new Paragraph("• " + task.getTitle() + " (" + status + ")",normalFont);
                            taskParagraph.setIndentationLeft(20f);
                            document.add(taskParagraph);


                            if (task.getSubtasks() != null) {
                                for (SubtaskEntity subtask : task.getSubtasks()) {
                                    String checkbox = subtask.isCompleted() ? "[x] " : "[ ] ";
                                    Paragraph subtaskParagraph = new Paragraph(checkbox + subtask.getTitle(), normalFont);
                                    subtaskParagraph.setIndentationLeft(40f);
                                    document.add(subtaskParagraph);
                                }
                            }

                        }
                    }
                }


                document.add(new Paragraph(" "));
            }

           document.close();

        } catch (DocumentException e) {
            throw new PdfGenerationException("Fout tijdens het genereren van de PDF-structuur.");
        }
        return outputStream.toByteArray();
    }
}