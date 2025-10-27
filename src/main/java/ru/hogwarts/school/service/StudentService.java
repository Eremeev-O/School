package ru.hogwarts.school.service;

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
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student addStudent(Student student){
        return studentRepository.save(student);
    }

    public Student findStudent(long id){
        return  studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " not found"));
    }

    public Student editStudent(Student student){
        if (student.getId() == null || !studentRepository.existsById(student.getId())) {
            throw new StudentBadRequestException("Student with id " + student.getId() + " not found");
        }
        return studentRepository.save(student);
    }

    public void delStudent(long id){
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException("Student with id " + id + " not found for deletion");
        }
        studentRepository.deleteById(id);
    }

    public Integer getStudentCount(){
        return studentRepository.getStudentCount();
    }

    public Float getStudentAvgAge(){
        return studentRepository.getStudentAvgAge();
    }

    public List<Student> getLastFiveStudent(){
        return studentRepository.getLastFiveStudent();
    }



    public Collection<Student> findByAge(int age) {
        if (age <= 0) {
            return Collections.emptyList();
        }
        return studentRepository.findByAge(age);
    }

    public Collection<Student> findByAgeBetween(int min, int max){
        if (min < 0 || max < 0 || min > max) {
            return Collections.emptyList();
        }
        return studentRepository.findByAgeBetween(min, max);
    }
}
