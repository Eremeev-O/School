package ru.hogwarts.school.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student createStudent(@RequestBody Student student){
        return studentService.addStudent(student);
    }

    @GetMapping("{id}")
    public Student getStudentInfo(@PathVariable Long id){
        return studentService.findStudent(id);
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student){
        return studentService.editStudent(student);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delStudent(@PathVariable Long id){
        studentService.delStudent(id);
    }

    @GetMapping
    public Collection<Student> findStudents(@RequestParam(required = false) Integer age, @RequestParam(required = false) Integer min, @RequestParam(required = false) Integer max) {
        if (age != null) {
            return studentService.findByAge(age);
        }
        if (min != null && max != null) {
            return studentService.findByAgeBetween(min, max);
        }
        return studentService.findByAge(0);
    }
}
