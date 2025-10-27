package ru.hogwarts.school.controller;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty createFaculty(@RequestBody Faculty faculty){
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("{id}")
    public Faculty getFacultyInfo(@PathVariable Long id){
        return facultyService.findFaculty(id);
    }


    @PutMapping
    public Faculty editFaculty(@RequestBody Faculty faculty){
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delFaculty(@PathVariable Long id){
        facultyService.delFaculty(id);
    }

    @GetMapping
    public Collection<Faculty> findFaculties(@RequestParam(required = false) String name, @RequestParam(required = false) String color) {
        if (color != null && (name == null || name.isBlank())) {
            return facultyService.findByColor(color);
        }
        if (name != null && (color == null || color.isBlank())) {
            return facultyService.findByName(name);
        }
        if (name != null && color != null) {
            return facultyService.findByNameAndColor(name, color);
        }
        return Collections.emptyList();
    }

    @GetMapping("{id}/students")
    public Collection<Student> getStudentsByFaculty(@PathVariable Long id) {
        return facultyService.getStudentsByFacultyId(id);
    }

    @GetMapping("/byStudent/{studentId}")
    public Faculty getFacultyByStudentId(@PathVariable Long studentId) {
        return facultyService.findFacultyByStudentId(studentId);
    }
}
