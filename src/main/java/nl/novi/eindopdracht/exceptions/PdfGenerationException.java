package nl.novi.eindopdracht.exceptions;

public class PdfGenerationException extends RuntimeException {
    public PdfGenerationException(String message) {
        super(message);
    }
}
