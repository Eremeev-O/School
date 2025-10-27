package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StudentBadRequestException extends RuntimeException {
    public StudentBadRequestException() {
        super("No such student");
    }

    public StudentBadRequestException(String message) {
        super(message);
    }
}