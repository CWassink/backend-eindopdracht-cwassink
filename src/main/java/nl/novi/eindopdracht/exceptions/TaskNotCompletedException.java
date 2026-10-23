package nl.novi.eindopdracht.exceptions;

public class TaskNotCompletedException extends RuntimeException {
    public TaskNotCompletedException(String message) {
        super(message);
    }
}
