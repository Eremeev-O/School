SELECT s.name, s.age, f.name
FROM student s
LEFT JOIN faculty f ON s.faculty_id = f.id;


SELECT s.name, s.age
FROM avatar a
LEFT JOIN student s ON a.student_id = s.id;