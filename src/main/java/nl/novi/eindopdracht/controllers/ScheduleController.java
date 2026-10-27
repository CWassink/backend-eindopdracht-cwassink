package nl.novi.eindopdracht.controllers;

import jakarta.validation.Valid;
import nl.novi.eindopdracht.dtos.schedule.ScheduleRequestDto;
import nl.novi.eindopdracht.dtos.schedule.ScheduleResponseDto;
import nl.novi.eindopdracht.services.PdfService;
import nl.novi.eindopdracht.services.ScheduleService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final PdfService pdfService;

    public ScheduleController(ScheduleService scheduleService, PdfService pdfService) {
        this.scheduleService = scheduleService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@Valid @RequestBody ScheduleRequestDto scheduleRequestDto, Authentication authentication) {
        ScheduleResponseDto newSchedule = scheduleService.createSchedule(scheduleRequestDto, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSchedule);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> getAllSchedules(Authentication authentication) {
        List<ScheduleResponseDto> schedules = scheduleService.getAllSchedules(authentication);

        return ResponseEntity.ok(schedules);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> getSchedule(@PathVariable Long id, Authentication authentication) {
        ScheduleResponseDto schedule = scheduleService.getScheduleById(id, authentication);

        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @Valid @RequestBody ScheduleRequestDto scheduleRequestDto, Authentication authentication) {
        ScheduleResponseDto schedule = scheduleService.updateSchedule(id, scheduleRequestDto, authentication);

        return ResponseEntity.ok(schedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id,  Authentication authentication) {
        scheduleService.deleteSchedule(id, authentication);

        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getSchedulePdf(@PathVariable Long id, Authentication authentication) {
        byte[] pdfBytes = pdfService.createSchedulePdf(id, authentication);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=schedule-" + id + ".pdf")
                .body(pdfBytes);
    }



}
