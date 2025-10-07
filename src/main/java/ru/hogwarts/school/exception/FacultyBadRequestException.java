package ru.hogwarts.school.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FacultyBadRequestException extends RuntimeException {
    public FacultyBadRequestException() {
        super("There is no such faculty");
    }

    public FacultyBadRequestException(String message) {
        super(message);
    }
}
