package com.iitu.zholdas_zhannura_student_api;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository repository;

    @Value("${app.version}")
    private String version;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // Приветственный эндпоинт с именем
    @GetMapping("/info")
    public String getInfo() {
        return "DevOps Midterm Project - Student API by Zholdas Zhannura";
    }

    @GetMapping("/version")
    public Map<String, String> getVersion() {
        return Map.of("application", "zholdas-zhannura-student-api", "version", version,
                "student", "Zhannura Zholdas", "group", "IT2-2306", "studentId", "37742");
    }

    // 1. Создать студента
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        // IDs are allocated by the database; POST must never overwrite an existing row.
        student.setId(null);
        Student saved = repository.save(student);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // 2. Получить всех студентов
    @GetMapping
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // 3. Получить студента по ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Обновить данные студента
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @Valid @RequestBody Student updated) {
        return repository.findById(id).map(student -> {
            student.setName(updated.getName());
            student.setEmail(updated.getEmail());
            return ResponseEntity.ok(repository.save(student));
        }).orElse(ResponseEntity.notFound().build());
    }

    // 5. Удалить студента
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
