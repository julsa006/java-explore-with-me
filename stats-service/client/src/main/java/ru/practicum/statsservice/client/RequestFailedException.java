package ru.practicum.statsservice.client;

import org.springframework.http.HttpStatus;

public class RequestFailedException extends RuntimeException {
    HttpStatus status;

    public RequestFailedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
