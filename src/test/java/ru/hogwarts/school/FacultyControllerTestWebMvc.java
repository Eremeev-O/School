package ru.hogwarts.school;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.exception.FacultyNotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
        import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerTestWebMvc {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "ТруЛЯЛЯ", "красный");
        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Faculty(null, "ТруЛЯЛЯ", "красный"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ТруЛЯЛЯ"))
                .andExpect(jsonPath("$.color").value("красный"));
    }

    @Test
    void testGetFacultyInfo() throws Exception {
        Faculty faculty = new Faculty(1L, "ТраЛЯЛЯ", "Зеленый");
        when(facultyService.findFaculty(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ТраЛЯЛЯ"))
                .andExpect(jsonPath("$.color").value("Зеленый"));
    }

    // Хотим получить информацию о несуществующем факультете
    @Test
    void testGetFacultyInfoNotFound() throws Exception {
        when(facultyService.findFaculty(anyLong())).thenThrow(new FacultyNotFoundException("Факультет с id 12345 отсутствует"));

        mockMvc.perform(get("/faculty/12345")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Факультет с id 12345 отсутствует"));
    }

    @Test
    void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "Угрюмые", "синий");
        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Faculty(1L, "Угрюмые", "синий"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Угрюмые"))
                .andExpect(jsonPath("$.color").value("синий"));
    }

    @Test
    void testDeleteFaculty() throws Exception {
        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testFindFacultiesByColor() throws Exception {
        Faculty faculty = new Faculty(1L, "ТруЛЯЛЯ", "красный");
        when(facultyService.findByColor(anyString())).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty?color=красный")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("ТруЛЯЛЯ"));
    }

    @Test
    void testFindFacultiesByName() throws Exception {
        Faculty faculty = new Faculty(1L, "Синявки", "синий");
        when(facultyService.findByName(anyString())).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty?name=Синявки")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Синявки"));
    }

    @Test
    void testFindFacultiesByNameAndColor() throws Exception {
        Faculty faculty = new Faculty(1L, "ТруЛЯЛЯ", "красный");
        when(facultyService.findByNameAndColor(anyString(), anyString())).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(get("/faculty?name=ТруЛЯЛЯ&color=красный")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("ТруЛЯЛЯ"));
    }

    @Test
    void testGetStudentsByFaculty() throws Exception {
        Student student1 = new Student(1L, "ГарриПотный", 15);
        Student student2 = new Student(2L, "Володя", 12);
        when(facultyService.getStudentsByFacultyId(anyLong())).thenReturn(List.of(student1, student2));

        mockMvc.perform(get("/faculty/1/students")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("ГарриПотный"))
                .andExpect(jsonPath("$[1].name").value("Володя"));
    }

    @Test
    void testGetFacultyByStudentId() throws Exception {
        Faculty faculty = new Faculty(1L, "ТруЛЯЛЯ", "зеленый");
        when(facultyService.findFacultyByStudentId(anyLong())).thenReturn(faculty);

        mockMvc.perform(get("/faculty/byStudent/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ТруЛЯЛЯ"));
    }
}
