package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findByColorIgnoreCase(String color);

    List<Faculty> findByNameIgnoreCaseOrColorIgnoreCase(String name, String color);

    Optional<Faculty> findByStudentsId(Long studentId);

    List<Faculty> findByNameIgnoreCase(String name);
}
