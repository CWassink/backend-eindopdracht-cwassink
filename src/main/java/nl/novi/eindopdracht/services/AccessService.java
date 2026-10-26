package nl.novi.eindopdracht.services;

import nl.novi.eindopdracht.entities.ScheduleEntity;
import nl.novi.eindopdracht.entities.UserEntity;
import nl.novi.eindopdracht.enums.Role;
import nl.novi.eindopdracht.exceptions.NoAccessException;
import org.springframework.stereotype.Service;

@Service
public class AccessService {



    public void checkScheduleAccess(UserEntity currentUser, ScheduleEntity schedule){
        if (schedule == null || schedule.getUser() == null || currentUser == null) {
            throw new NoAccessException("Toegang geweigerd: Ongeldige gegevens.");
        }
        if (Role.CLIENT.equals(currentUser.getRole())) {
            if (!schedule.getUser().getId().equals(currentUser.getId())) {
                throw new NoAccessException("Toegang geweigerd: Je hebt geen toegang tot het schema van een andere cliënt.");
            }
        }
        else if (Role.CAREGIVER.equals(currentUser.getRole())) {
            UserEntity scheduleOwner = schedule.getUser();

            if (scheduleOwner.getCaregiver() == null || !scheduleOwner.getCaregiver().getId().equals(currentUser.getId())) {
                throw new NoAccessException("Toegang geweigerd: Deze cliënt is niet aan jou gekoppeld.");
            }
        }
        else {
            throw new NoAccessException("Toegang geweigerd: Onbekende gebruikersrol.");
        }

    }
}
