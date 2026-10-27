package nl.novi.eindopdracht.services;


import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.exceptions.PdfGenerationException;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.repositories.ScheduleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;

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
                .orElseThrow(()-> new RecordNotFoundException("Schema met id " + scheduleId + " niet gevonden."));

        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, schedule);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        Document document = new Document();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Dagschema: " + schedule.getName()));
            document.close();
        } catch (DocumentException e) {
            throw new PdfGenerationException("Fout tijdens het genereren van de PDF-structuur");
        }
        return outputStream.toByteArray();
    }
}
