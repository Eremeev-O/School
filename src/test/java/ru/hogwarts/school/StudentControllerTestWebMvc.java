package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.exception.StudentNotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerTestWebMvc {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private StudentController studentController;

    @Test
    void testCreateStudent() throws Exception {
        Student student = new Student(1L, "ГарриПотный", 16);
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Student(null, "ГарриПотный", 16))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ГарриПотный"))
                .andExpect(jsonPath("$.age").value(16));
    }

    @Test
    void testGetStudentInfo() throws Exception {
        Student student = new Student(1L, "Санько", 19);
        when(studentService.findStudent(anyLong())).thenReturn(student);

        mockMvc.perform(get("/student/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Санько"))
                .andExpect(jsonPath("$.age").value(19));
    }

    // Хотим получить информацию о несуществующем студенте
    @Test
    void testGetStudentInfoNotFound() throws Exception {
        when(studentService.findStudent(anyLong())).thenThrow(new StudentNotFoundException("Студент с id 12345 не существует"));

        mockMvc.perform(get("/student/12345")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Студент с id 12345 не существует"));
    }

    @Test
    void testEditStudent() throws Exception {
        Student student = new Student(1L, "Рамон", 24);
        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Student(1L, "Рамон", 24))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Рамон"))
                .andExpect(jsonPath("$.age").value(24));
    }

    @Test
    void testDeleteStudent() throws Exception {
        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindStudentsByAge() throws Exception {
        Student student1 = new Student(1L, "ГарриРотный", 15);
        Student student2 = new Student(2L, "Гиви", 15);
        when(studentService.findByAge(15)).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student?age=15")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("ГарриРотный"))
                .andExpect(jsonPath("$[1].name").value("Гиви"));
    }

    @Test
    void testFindStudentsByAgeBetween() throws Exception {
        Student student1 = new Student(1L, "Гиви", 16);
        Student student2 = new Student(2L, "ГарриПотный", 21);
        when(studentService.findByAgeBetween(16, 21)).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/student?min=16&max=21")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гиви"))
                .andExpect(jsonPath("$[1].name").value("ГарриПотный"));
    }

    @Test
    void testFindStudentsNoParams() throws Exception {
        Student student = new Student(1L, "ГарриПотный", 21);
        when(studentService.findByAge(0)).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/student")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

    }
}
