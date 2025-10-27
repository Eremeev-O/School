package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.hogwarts.school.exception.FacultyBadRequestException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.exception.StudentBadRequestException;
import ru.hogwarts.school.exception.StudentNotFoundException;

@RestControllerAdvice(basePackages = "ru.hogwarts.school.controller")
public class AppExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(FacultyNotFoundException.class)
    public ResponseEntity<String> handleFacultyNotFoundException(FacultyNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(StudentBadRequestException.class)
    public ResponseEntity<String> handleStudentBadRequestException(StudentBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(FacultyBadRequestException.class)
    public ResponseEntity<String> handleFacultyBadRequestException(FacultyBadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
