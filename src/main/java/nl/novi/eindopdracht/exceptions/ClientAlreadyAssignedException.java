package nl.novi.eindopdracht.exceptions;

public class ClientAlreadyAssignedException extends RuntimeException {
    public ClientAlreadyAssignedException(String message) {
        super(message);
    }
}
