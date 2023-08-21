package ru.practicum.ewmservice.exceptions;

public class RequestConditionException extends RuntimeException {
    public RequestConditionException(String message) {
        super(message);
    }
}
