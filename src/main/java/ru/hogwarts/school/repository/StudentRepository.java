package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.List;


public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAge(int age);

    List<Student> findByAgeBetween(Integer min, Integer max);

    List<Student> findByFacultyId(Long facultyId);

    @Query(value = "select count(id) from student", nativeQuery = true)
    Integer getStudentCount();

    @Query(value = "select avg(age) from student", nativeQuery = true)
    Float getStudentAvgAge();

    @Query(value = "select * from student order by id desc limit 5", nativeQuery = true)
    List<Student> getLastFiveStudent();

}
