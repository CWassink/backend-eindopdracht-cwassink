package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.Role;
import nl.novi.eindopdracht.exceptions.NoAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccessServiceTest {

    private AccessService accessService;
    private UserEntity caregiver;
    private UserEntity client;
    private ScheduleEntity schedule;

    @BeforeEach
    void setUp() {

        caregiver = new UserEntity();
        caregiver.setId(1L);
        caregiver.setRole(Role.CAREGIVER);

        client = new UserEntity();
        client.setId(2L);
        client.setRole(Role.CLIENT);
        client.setCaregiver(caregiver);

        schedule = new ScheduleEntity();
        schedule.setUser(client);

        accessService = new AccessService();
    }

    @Test
    public void checkScheduleAccess_shouldNotThrow_whenClientOwnsSchedule() {
        // Arrange: zie setUp

        // Act & Assert
        assertDoesNotThrow(() -> accessService.checkScheduleAccess(client, schedule));
    }

    @Test
    public void checkScheduleAccess_shouldNotThrow_whenCaregiverOwnsClient() {
        // Arrange: zie setUp

        // Act & Assert
        assertDoesNotThrow(() -> accessService.checkScheduleAccess(caregiver, schedule));
    }

    @Test
    public void checkScheduleAccess_shouldThrowException_whenOtherClientRequestsSchedule() {
        // Arrange
        UserEntity otherClient = new UserEntity();
        otherClient.setId(3L);
        otherClient.setRole(Role.CLIENT);

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkScheduleAccess(otherClient, schedule));
    }

    @Test
    public void checkScheduleAccess_shouldThrowException_whenOtherCaregiverRequestsSchedule() {
        // Arrange
        UserEntity otherCaregiver = new UserEntity();
        otherCaregiver.setId(5L);
        otherCaregiver.setRole(Role.CAREGIVER);

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkScheduleAccess(otherCaregiver, schedule));
    }

    @Test
    public void checkScheduleAccess_shouldThrowException_whenCaregiverNotAssignedToClient() {
        // Arrange: verbreek de koppeling tussen de client en de caregiver
        client.setCaregiver(null);

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkScheduleAccess(caregiver, schedule));
    }

    @Test
    public void checkScheduleAccess_shouldThrowException_whenCurrentUserIsNull() {
        // Arrange: zie setUp (geen extra aanpassingen nodig)

        // Act & Assert: we geven null mee als eerste parameter
        assertThrows(NoAccessException.class, () -> accessService.checkScheduleAccess(null, schedule));
    }

    @Test
    public void checkScheduleAccess_shouldThrowException_whenUserHasNoRole() {
        // Arrange: maak een gebruiker zonder rol aan
        UserEntity userWithoutRole = new UserEntity();
        userWithoutRole.setId(6L);
        // We roepen bewust geen setRole() aan

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkScheduleAccess(userWithoutRole, schedule));
    }

    @Test
    public void checkClientAccess_shouldNotThrow_whenCaregiverOwnsClient() {
        // Arrange: zie setUp

        // Act & Assert
        assertDoesNotThrow(() -> accessService.checkClientAccess(caregiver, client));
    }

    @Test
    public void checkClientAccess_shouldThrowException_whenClientIsNull() {
        // Arrange: zie setUp

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkClientAccess(caregiver, null));
    }

    @Test
    public void checkClientAccess_shouldThrowException_whenTargetUserIsNotAClient() {
        // Arrange
        UserEntity wrongClient = new UserEntity();
        wrongClient.setId(4L);
        wrongClient.setRole(Role.CAREGIVER);

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkClientAccess(caregiver, wrongClient));
    }

    @Test
    public void checkClientAccess_shouldThrowException_whenOtherCaregiverRequestsClient() {
        // Arrange
        UserEntity otherCaregiver = new UserEntity();
        otherCaregiver.setId(5L);
        otherCaregiver.setRole(Role.CAREGIVER);

        // Act & Assert
        assertThrows(NoAccessException.class, () -> accessService.checkClientAccess(otherCaregiver, client));
    }
}