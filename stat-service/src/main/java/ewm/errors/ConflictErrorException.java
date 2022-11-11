package ewm.errors;

public class ConflictErrorException extends RuntimeException {
    public ConflictErrorException(String message) {
        super(message);
    }
}
