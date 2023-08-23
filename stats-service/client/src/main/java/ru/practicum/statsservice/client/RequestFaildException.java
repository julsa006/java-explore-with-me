package ru.practicum.statsservice.client;

import org.springframework.http.HttpStatus;

public class RequestFaildException extends RuntimeException {
    HttpStatus status;

    public RequestFaildException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
