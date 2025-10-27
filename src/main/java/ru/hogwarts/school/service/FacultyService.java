package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.FacultyBadRequestException;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty){
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id){
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Faculty with id " + id + " not found"));
    }

    public Faculty editFaculty(Faculty faculty){
        if (faculty.getId() == null && !facultyRepository.existsById(faculty.getId())) {
            throw new FacultyBadRequestException("Faculty with id " + faculty.getId() + " not found");
        }
        return facultyRepository.save(faculty);
    }

    public void delFaculty(long id){
        if (!facultyRepository.existsById(id)) {
            throw new FacultyNotFoundException("Faculty with id " + id + " not found for deletion");
        }
        facultyRepository.deleteById(id);
    }


    public Collection<Faculty> findByColor(String color) {
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Faculty> findByName(String name) {
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Faculty> findByNameAndColor(String name, String color){
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Faculty findFacultyByStudentId(Long studentId) {
        return facultyRepository.findByStudentsId(studentId).orElseThrow(() -> new FacultyNotFoundException("Faculty for student with id " + studentId + " not found"));
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        if (!facultyRepository.existsById(facultyId)) {
            throw new FacultyNotFoundException("Faculty with id " + facultyId + " not found");
        }
        return studentRepository.findByFacultyId(facultyId);
    }
}
