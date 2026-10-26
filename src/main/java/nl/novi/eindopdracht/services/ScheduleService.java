package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.schedule.ScheduleRequestDto;
import nl.novi.eindopdracht.dtos.schedule.ScheduleResponseDto;
import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.ScheduleMapper;
import nl.novi.eindopdracht.repositories.ScheduleRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final ScheduleMapper scheduleMapper;
    private final AccessService accessService;
    private final UserService userService;

    public ScheduleService(ScheduleRepository scheduleRepository, UserRepository userRepository, ScheduleMapper scheduleMapper, AccessService accessService, UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.scheduleMapper = scheduleMapper;
        this.accessService = accessService;
        this.userService = userService;
    }

    public ScheduleResponseDto createSchedule(ScheduleRequestDto scheduleRequestDto, Authentication authentication) {
        UserEntity user = userRepository.findById(scheduleRequestDto.getUserId())
                .orElseThrow(() -> new RecordNotFoundException("Gebruiker met id " + scheduleRequestDto.getUserId() + " niet gevonden."));
        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkClientAccess(currentUser, user);

        ScheduleEntity schedule = scheduleMapper.transferToEntity(scheduleRequestDto);
        schedule.setUser(user);

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);

        return scheduleMapper.transferToDto(savedSchedule);

    }

    public List<ScheduleResponseDto> getAllSchedules(Authentication authentication) {
        UserEntity currentUser = userService.getCurrentUser(authentication);

        List<ScheduleEntity> schedules = scheduleRepository.findByUserCaregiverId(currentUser.getId());
        List<ScheduleResponseDto> scheduleResponseDtos = new ArrayList<>();

        for (ScheduleEntity schedule : schedules) {
            scheduleResponseDtos.add(scheduleMapper.transferToDto(schedule));
        }
        return scheduleResponseDtos;

    }

    public ScheduleResponseDto getScheduleById(Long id, Authentication authentication) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Schema met id " + id + " niet gevonden."));
        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, schedule);

        return scheduleMapper.transferToDto(schedule);
    }

    public ScheduleResponseDto updateSchedule(Long id, ScheduleRequestDto scheduleRequestDto, Authentication authentication) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Schema met id " + id + " niet gevonden."));
        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, schedule);

        schedule.setName(scheduleRequestDto.getName());
        schedule.setDate(scheduleRequestDto.getDate());

        ScheduleEntity savedSchedule = scheduleRepository.save(schedule);
        return scheduleMapper.transferToDto(savedSchedule);

    }


    public void deleteSchedule(Long id, Authentication authentication) {
        ScheduleEntity schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Schema met id " + id + " niet gevonden."));

        UserEntity currentUser = userService.getCurrentUser(authentication);
        accessService.checkScheduleAccess(currentUser, schedule);


        scheduleRepository.deleteById(id);
    }

}