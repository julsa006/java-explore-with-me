package ru.practicum.ewmservice.exceptions;

public class IllegalEntityStateException extends RuntimeException {
    public IllegalEntityStateException(String message) {
        super(message);
    }
}
