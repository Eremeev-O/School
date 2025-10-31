package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    Logger logger = LoggerFactory.getLogger(FacultyService.class);
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }

    public Faculty addFaculty(Faculty faculty){
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id){
        logger.info("Was invoked method for find faculty");
        return facultyRepository.findById(id).orElseThrow(() -> new FacultyNotFoundException("Faculty with id " + id + " not found"));
    }

    public Faculty editFaculty(Faculty faculty){
        logger.info("Was invoked method for edit faculty");
        Long facultyId = faculty.getId();
        if (facultyId == null && !facultyRepository.existsById(facultyId)) {
            logger.warn("Faculty with id {} not found", facultyId);
            throw new FacultyBadRequestException("Faculty with id " + facultyId + " not found");
        }
        return facultyRepository.save(faculty);
    }

    public void delFaculty(long id){
        logger.info("Was invoked method for delete faculty");
        if (!facultyRepository.existsById(id)) {
            logger.warn("Faculty with id {} not found for deletion", id);
            throw new FacultyNotFoundException("Faculty with id " + id + " not found for deletion");
        }
        facultyRepository.deleteById(id);
    }


    public Collection<Faculty> findByColor(String color) {
        logger.info("Was invoked method for find by color faculty");
        return facultyRepository.findByColorIgnoreCase(color);
    }

    public Collection<Faculty> findByName(String name) {
        logger.info("Was invoked method for find by name faculty");
        return facultyRepository.findByNameIgnoreCase(name);
    }

    public Collection<Faculty> findByNameAndColor(String name, String color){
        logger.info("Was invoked method for find by name and color faculty");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Faculty findFacultyByStudentId(Long studentId) {
        logger.info("Was invoked method for find faculty by student");
        return facultyRepository.findByStudentsId(studentId).orElseThrow(() -> new FacultyNotFoundException("Faculty for student with id " + studentId + " not found"));
    }

    public Collection<Student> getStudentsByFacultyId(Long facultyId) {
        logger.info("Was invoked method for get student by faculty");
        if (!facultyRepository.existsById(facultyId)) {
            logger.warn("Faculty with id {} not found", facultyId);
            throw new FacultyNotFoundException("Faculty with id " + facultyId + " not found");
        }
        return studentRepository.findByFacultyId(facultyId);
    }
}
