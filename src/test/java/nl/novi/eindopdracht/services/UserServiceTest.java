package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.dtos.user.UserResponseDto;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.Role;
import nl.novi.eindopdracht.exceptions.ClientAlreadyAssignedException;
import nl.novi.eindopdracht.exceptions.NoAccessException;
import nl.novi.eindopdracht.exceptions.RecordNotFoundException;
import nl.novi.eindopdracht.mappers.UserMapper;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserService userService;

    @Test
    public void getCurrentUser_shouldReturnUser_whenUserExists() {
        // Arrange
        UserEntity caregiver1 = new UserEntity();
        caregiver1.setUsername("caregiver1");

        when(authentication.getName()).thenReturn("caregiver1");
        when(userRepository.findByUsername("caregiver1")).thenReturn(Optional.of(caregiver1));

        // Act
        UserEntity result = userService.getCurrentUser(authentication);

        // Assert
        assertEquals(caregiver1, result);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void getCurrentUser_shouldCreateAndReturnNewCaregiver_whenUserDoesNotExist() {
        // Arrange
        when(authentication.getName()).thenReturn("caregiver2");
        when(userRepository.findByUsername("caregiver2")).thenReturn(Optional.empty());

        doReturn(List.of(new SimpleGrantedAuthority("CAREGIVER")))
                .when(authentication).getAuthorities();

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userService.getCurrentUser(authentication);

        // Assert
        assertEquals("caregiver2", result.getUsername());
        assertEquals(Role.CAREGIVER, result.getRole());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void getCurrentUser_shouldCreateAndReturnNewClient_whenUserDoesNotExist() {
        // Arrange
        when(authentication.getName()).thenReturn("newClient");
        when(userRepository.findByUsername("newClient")).thenReturn(Optional.empty());

        doReturn(List.of(new SimpleGrantedAuthority("CLIENT")))
                .when(authentication).getAuthorities();

        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        UserEntity result = userService.getCurrentUser(authentication);

        // Assert
        assertEquals("newClient", result.getUsername());
        assertEquals(Role.CLIENT, result.getRole());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void getCurrentUserDto_shouldReturnDto_whenUserExists() {
        // Arrange
        UserEntity caregiver = new UserEntity();
        caregiver.setUsername("caregiver1");
        UserResponseDto expectedDto = new UserResponseDto();

        when(authentication.getName()).thenReturn("caregiver1");
        when(userRepository.findByUsername("caregiver1")).thenReturn(Optional.of(caregiver));
        when(userMapper.transferToDto(caregiver)).thenReturn(expectedDto);

        // Act
        UserResponseDto result = userService.getCurrentUserDto(authentication);

        // Assert
        assertEquals(expectedDto, result);
    }

    @Test
    public void linkClient_shouldLinkCaregiverToClient_whenDataIsValid() {
        // Arrange
        UserEntity caregiver = new UserEntity();
        caregiver.setUsername("caregiver1");
        caregiver.setRole(Role.CAREGIVER);

        UserEntity client = new UserEntity();
        client.setUsername("client1");
        client.setRole(Role.CLIENT);
        client.setCaregiver(null);

        UserResponseDto expectedDto = new UserResponseDto();

        when(authentication.getName()).thenReturn("caregiver1");
        when(userRepository.findByUsername("caregiver1")).thenReturn(Optional.of(caregiver));
        when(userRepository.findByUsername("client1")).thenReturn(Optional.of(client));
        when(userRepository.save(client)).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.transferToDto(client)).thenReturn(expectedDto);

        // Act
        UserResponseDto result = userService.linkClient("client1", authentication);

        // Assert
        assertEquals(caregiver, client.getCaregiver());
        assertEquals(expectedDto, result);
        verify(userRepository, times(1)).save(client);
    }

    @Test
    public void linkClient_shouldThrowRecordNotFoundException_whenClientUsernameDoesNotExist() {
        // Arrange
        UserEntity caregiver = new UserEntity();
        caregiver.setUsername("caregiver1");

        when(authentication.getName()).thenReturn("caregiver1");
        when(userRepository.findByUsername("caregiver1")).thenReturn(Optional.of(caregiver));
        when(userRepository.findByUsername("nonExistingClient")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RecordNotFoundException.class, () -> userService.linkClient("nonExistingClient", authentication));
    }

    @Test
    public void linkClient_shouldThrowNoAccessException_whenTargetUserIsNotAClient() {
        // Arrange
        UserEntity caregiver = new UserEntity();
        caregiver.setUsername("caregiver1");

        UserEntity wrongClient = new UserEntity();
        wrongClient.setUsername("otherCaregiver");
        wrongClient.setRole(Role.CAREGIVER);

        when(authentication.getName()).thenReturn("caregiver1");
        when(userRepository.findByUsername("caregiver1")).thenReturn(Optional.of(caregiver));
        when(userRepository.findByUsername("otherCaregiver")).thenReturn(Optional.of(wrongClient));

        // Act & Assert
        assertThrows(NoAccessException.class, () -> userService.linkClient("otherCaregiver", authentication));
    }

    @Test
    public void linkClient_shouldThrowClientAlreadyAssignedException_whenClientAlreadyHasCaregiver() {
        // Arrange
        UserEntity caregiver = new UserEntity();
        caregiver.setUsername("caregiver1");

        UserEntity alreadyAssignedCaregiver = new UserEntity();
        alreadyAssignedCaregiver.setUsername("caregiver2");

        UserEntity client = new UserEntity();
        client.setUsername("client1");
        client.setRole(Role.CLIENT);
        client.setCaregiver(alreadyAssignedCaregiver);

        when(authentication.getName()).thenReturn("caregiver1");
        when(userRepository.findByUsername("caregiver1")).thenReturn(Optional.of(caregiver));
        when(userRepository.findByUsername("client1")).thenReturn(Optional.of(client));

        // Act & Assert
        assertThrows(ClientAlreadyAssignedException.class, () -> userService.linkClient("client1", authentication));
    }

}

