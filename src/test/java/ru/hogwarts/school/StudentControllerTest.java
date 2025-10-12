package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTest{

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private AvatarRepository avatarRepository;
    @Autowired
    private StudentService studentService;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/student";
        avatarRepository.deleteAll();
        studentRepository.deleteAll();


    }

    @AfterEach
    public void teardown() {
        avatarRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() {
        Student student = new Student(null, "Кени", 13);
        ResponseEntity<Student> response = restTemplate.postForEntity(baseUrl, student, Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("Кени");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(13);
    }

    @Test
    void testGetStudentInfo() {
        Student student = studentService.addStudent(new Student(null, "Володя", 12));
        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + student.getId(), Student.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Володя");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(12);
    }

    @Test
    void testEditStudent() {
        Student student = studentService.addStudent(new Student(null, "Алексий", 13));
        student.setName("Серожа");
        student.setAge(14);

        restTemplate.put(baseUrl, student);

        ResponseEntity<Student> response = restTemplate.getForEntity(baseUrl + "/" + student.getId(), Student.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Серожа");
        Assertions.assertThat(response.getBody().getAge()).isEqualTo(14);
    }

    // Хотим обновить несуществующего студента
    @Test
    void testEditStudentNotFound() {
        Student nonExistentStudent = new Student(12345L, "Shadow", 20);
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, org.springframework.http.HttpMethod.PUT, new org.springframework.http.HttpEntity<>(nonExistentStudent), String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertThat(response.getBody()).contains("Student with id 12345 not found");
    }

    @Test
    void testDeleteStudent() {
        Student student = studentService.addStudent(new Student(null, "Максут", 33));

        restTemplate.delete(baseUrl + "/" + student.getId());

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + student.getId(), String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindStudentsByAge() {
        studentService.addStudent(new Student(null, "Али Баба", 10));
        studentService.addStudent(new Student(null, "Нурсултан", 11));
        studentService.addStudent(new Student(null, "Жорик", 10));

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "?age=10", List.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
    }

    @Test
    void testFindStudentsByAgeBetween() {
        studentService.addStudent(new Student(null, "Али Баба", 10));
        studentService.addStudent(new Student(null, "Нурсултан", 11));
        studentService.addStudent(new Student(null, "Жорик", 12));

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "?min=10&max=11", List.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
    }
}