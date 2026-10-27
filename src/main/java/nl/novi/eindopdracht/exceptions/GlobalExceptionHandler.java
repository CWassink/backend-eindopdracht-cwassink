package nl.novi.eindopdracht.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecordNotFoundException.class)
    public ResponseEntity<String> handleRecordNotFoundException(RecordNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);


        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ongeldige invoer. Controleer of alle velden kloppen, bijvoorbeeld dayPart: OCHTEND, MIDDAG, AVOND, en datum als JJJJ-MM-DD");
    }

    @ExceptionHandler(TaskNotCompletedException.class)
    public ResponseEntity<String> handleTaskNotCompletedException(TaskNotCompletedException ex) {
       return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(NoAccessException.class)
    public ResponseEntity<String> handleNoAccessException(NoAccessException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ClientAlreadyAssignedException.class)
    public ResponseEntity<String> handleClientAlreadyAssignedException(ClientAlreadyAssignedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidFileException.class)
    public ResponseEntity<String> handleInvalidFileException(InvalidFileException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        return ResponseEntity
                .status(HttpStatus.PAYLOAD_TOO_LARGE) // Status 413
                .body("Het bestand mag maximaal 2 MB zijn.");
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<String> handlePdfGenerationException(PdfGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

}
