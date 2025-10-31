package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.StudentBadRequestException;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class StudentService {
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student){
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id){
        logger.info("Was invoked method for find student");
        return  studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
    }

    public Student editStudent(Student student){
        logger.info("Was invoked method for edit student");
        Long studentId = student.getId();
        if (studentId == null || !studentRepository.existsById(studentId)) {
            logger.warn("Student with id {} not found", studentId);
            throw new StudentBadRequestException("Student with id " + studentId + " not found");
        }
        return studentRepository.save(student);
    }

    public void delStudent(long id){
        logger.info("Was invoked method for delete student");
        if (!studentRepository.existsById(id)) {
            logger.warn("Student with id {} not found for deletion", id);
            throw new StudentNotFoundException("Student with id " + id + " not found for deletion");
        }
        studentRepository.deleteById(id);
    }

    public Integer getStudentCount(){
        logger.info("Was invoked method for count student");
        return studentRepository.getStudentCount();
    }

    public Float getStudentAvgAge(){
        logger.info("Was invoked method for age student");
        return studentRepository.getStudentAvgAge();
    }

    public List<Student> getLastFiveStudent(){
        logger.info("Was invoked method for five student");
        return studentRepository.getLastFiveStudent();
    }



    public Collection<Student> findByAge(int age) {
        logger.info("Was invoked method for find by age student");
        if (age <= 0) {
            return Collections.emptyList();
        }
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max){
        logger.info("Was invoked method for find by age (between) student");
        if (min < 0 || max < 0 || min > max) {
            return Collections.emptyList();
        }
        return studentRepository.findByAgeBetween(min, max);
    }
}
