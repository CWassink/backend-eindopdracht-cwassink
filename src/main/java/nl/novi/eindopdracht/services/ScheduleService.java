package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.schedule.ScheduleRequestDto;
import nl.novi.eindopdracht.dtos.schedule.ScheduleResponseDto;
import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.ScheduleMapper;
import nl.novi.eindopdracht.repositories.ScheduleRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.scheduleMapper = scheduleMapper;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto) {
        UserEntity user = userRepository.findById(scheduleRequestDto.getUserId())
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker met id " + scheduleRequestDto.getUserId() + " niet gevonden."));

        ScheduleEntity schedule = scheduleMapper.transferToEntity(scheduleRequestDto);
        schedule.setUser(user);

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);

        return scheduleMapper.transferToDto(savedSchedule);

    }

    public List<ScheduleResponseDto> getAllSchedules() {
        List<ScheduleEntity> schedules = scheduleRepository.findAll();
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();

        for (ScheduleEntity schedule : schedules) {
            scheduleResponseDtos.add(scheduleMapper.transferToDto(schedule));
        }
        return scheduleResponseDtos;

    }

    public ScheduleResponseDto getScheduleById(Long id) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Schema met id" + id + " niet gevonden."));

        return scheduleMapper.transferToDto(schedule);
    }

    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Schema met id" + id + " niet gevonden."));
        schedule.setName(scheduleRequestDto.getName());
        schedule.setDate(scheduleRequestDto.getDate());

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.transferToDto(savedSchedule);

    }


    public void deleteSchedule(Long id) {

        if (!scheduleRepository.existsById(id)) {
            throw new RecordNotFoundException("Schema met id " + id + " niet gevonden.");
        }

        scheduleRepository.deleteById(id);
    }

}