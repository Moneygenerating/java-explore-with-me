package ru.practicum.errors;

public class ConflictErrorException extends RuntimeException {
    public ConflictErrorException(String message) {
        super(message);
    }
}
