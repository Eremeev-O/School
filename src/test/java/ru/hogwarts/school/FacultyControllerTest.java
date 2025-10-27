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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Objects;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private FacultyService facultyService;
    @Autowired
    private StudentService studentService;

    private String baseUrl;

    @BeforeEach
    public void setup() {
        baseUrl = "http://localhost:" + port + "/faculty";
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @AfterEach
    public void teardown() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    @Test
    void testCreateFaculty() {
        Faculty faculty = new Faculty(null, "ТруЛЯЛЯ", "красный");
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getId()).isNotNull();
        Assertions.assertThat(response.getBody().getName()).isEqualTo("ТруЛЯЛЯ");
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("красный");
    }

    @Test
    void testGetFacultyInfo() {
        Faculty faculty = facultyService.addFaculty(new Faculty(null, "ТраЛЯЛЯ", "зеленый"));
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("ТраЛЯЛЯ");
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("зеленый");
    }

    @Test
    void testEditFaculty() {
        Faculty faculty = facultyService.addFaculty(new Faculty(null, "Веселые", "Желтый"));
        faculty.setName("Веселые2");
        faculty.setColor("голубой");

        restTemplate.put(baseUrl, faculty);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), Faculty.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("Веселые2");
        Assertions.assertThat(response.getBody().getColor()).isEqualTo("голубой");
    }

    @Test
    void testDeleteFaculty() {
        Faculty faculty = facultyService.addFaculty(new Faculty(null, "Чумарные", "черные"));

        restTemplate.delete(baseUrl + "/" + faculty.getId());

        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId(), String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    // Хотим удалить отсутствующий факультет
    @Test
    void testDeleteFacultyNotFound() {
        restTemplate.delete(baseUrl + "/12345");
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/12345", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindFacultiesByColor() {
        facultyService.addFaculty(new Faculty(null, "Чумарные", "черные"));
        facultyService.addFaculty(new Faculty(null, "Веселые2", "голубой"));
        facultyService.addFaculty(new Faculty(null, "ТраЛЯЛЯ", "зеленый"));
        facultyService.addFaculty(new Faculty(null, "ТруЛЯЛЯ", "красный"));

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "?color=зеленый", List.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);
    }

    @Test
    void testFindFacultiesByName() {
        facultyService.addFaculty(new Faculty(null, "Чумарные", "черные"));
        facultyService.addFaculty(new Faculty(null, "ТраЛЯЛЯ", "зеленый"));

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "?name=Чумарные", List.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);
    }

    @Test
    void testFindFacultiesByNameAndColor() {
        facultyService.addFaculty(new Faculty(null, "Чумарные", "черные"));
        facultyService.addFaculty(new Faculty(null, "ТраЛЯЛЯ", "зеленый"));
        facultyService.addFaculty(new Faculty(null, "Чумарные", "красный"));

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "?name=Gryffindor&color=красный", List.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(1);
    }

    @Test
    void testGetStudentsByFaculty() {
        Faculty faculty = facultyService.addFaculty(new Faculty(null, "ТраЛЯЛЯ", "зеленый"));
        Student student1 = new Student(null, "Висло", 11);
        student1.setFaculty(faculty);
        studentService.addStudent(student1);
        Student student2 = new Student(null, "ЧегоСла", 11);
        student2.setFaculty(faculty);
        studentService.addStudent(student2);

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl + "/" + faculty.getId() + "/students", List.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).size()).isEqualTo(2);
    }

    // Хотим получить студентов отсутствующего факультета
    @Test
    void testGetStudentsByNonExistentFaculty() {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/12345/students", String.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getBody()).contains("Faculty with id 12345 not found");
    }

    @Test
    void testGetFacultyByStudentId() {
        Faculty faculty = facultyService.addFaculty(new Faculty(null, "ТраЛЯЛЯ", "зеленый"));
        Student student = new Student(null, "Висло", 11);
        student.setFaculty(faculty);
        studentService.addStudent(student);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/byStudent/" + student.getId(), Faculty.class);

        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo("ТраЛЯЛЯ");
    }
}