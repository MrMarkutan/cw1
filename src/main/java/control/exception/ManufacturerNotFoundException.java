package control.exception;

public class ManufacturerNotFoundException extends RuntimeException {
    public ManufacturerNotFoundException(String message) {
        super(message);
    }

    public ManufacturerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
