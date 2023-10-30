package control.exception;

public class SouvenirNotFoundException extends RuntimeException {
    public SouvenirNotFoundException(String message) {
        super(message);
    }

    public SouvenirNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
