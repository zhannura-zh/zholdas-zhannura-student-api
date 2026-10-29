package com.iitu.zholdas_zhannura_student_api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class StudentApiTests {
    @Autowired MockMvc mvc;
    @Autowired StudentRepository repository;

    @BeforeEach void clearTestDatabase() { repository.deleteAll(); }

    @Test void displaysStudentIdentity() throws Exception {
        mvc.perform(get("/api/students/info")).andExpect(status().isOk())
                .andExpect(content().string("DevOps Midterm Project - Student API by Zholdas Zhannura"));
    }

    @Test void exposesPersonalizedVersion() throws Exception {
        mvc.perform(get("/api/students/version")).andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value("1.1.0"))
                .andExpect(jsonPath("$.studentId").value("37742"));
    }

    @Test void createsStudent() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Zhannura Zholdas\",\"email\":\"zhannura@example.test\"}"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Zhannura Zholdas"));
        assertThat(repository.count()).isEqualTo(1);
    }

    @Test void postDoesNotOverwriteClientSuppliedId() throws Exception {
        Student existing = repository.save(new Student("Original", "original@example.test"));
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":" + existing.getId() + ",\"name\":\"Zhannura\",\"email\":\"new@example.test\"}"))
                .andExpect(status().isCreated());
        assertThat(repository.count()).isEqualTo(2);
        assertThat(repository.findById(existing.getId()).orElseThrow().getName()).isEqualTo("Original");
    }

    @Test void listsStudents() throws Exception {
        repository.save(new Student("Zhannura", "list@example.test"));
        mvc.perform(get("/api/students")).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Zhannura"));
    }

    @Test void retrievesStudentById() throws Exception {
        Student student = repository.save(new Student("Zhannura", "get@example.test"));
        mvc.perform(get("/api/students/{id}", student.getId())).andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("get@example.test"));
    }

    @Test void missingStudentReturns404() throws Exception {
        mvc.perform(get("/api/students/999999999")).andExpect(status().isNotFound());
    }

    @Test void updatesStudent() throws Exception {
        Student student = repository.save(new Student("Zhannura", "update@example.test"));
        mvc.perform(put("/api/students/{id}", student.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Zhannura Zholdas Updated\",\"email\":\"updated@example.test\"}"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.name").value("Zhannura Zholdas Updated"));
        assertThat(repository.findById(student.getId()).orElseThrow().getEmail()).isEqualTo("updated@example.test");
    }

    @Test void updateMissingStudentReturns404() throws Exception {
        mvc.perform(put("/api/students/999999999").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Zhannura\",\"email\":\"missing@example.test\"}"))
                .andExpect(status().isNotFound());
    }

    @Test void deletesStudent() throws Exception {
        Student student = repository.save(new Student("Zhannura", "delete@example.test"));
        mvc.perform(delete("/api/students/{id}", student.getId())).andExpect(status().isNoContent());
        assertThat(repository.existsById(student.getId())).isFalse();
    }

    @Test void deleteMissingStudentReturns404() throws Exception {
        mvc.perform(delete("/api/students/999999999")).andExpect(status().isNotFound());
    }

    @Test void blankNameReturns400() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\" \",\"email\":\"valid@example.test\"}"))
                .andExpect(status().isBadRequest());
        assertThat(repository.count()).isZero();
    }

    @Test void invalidEmailReturns400() throws Exception {
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Zhannura\",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test void invalidUpdateDoesNotChangeRecord() throws Exception {
        Student student = repository.save(new Student("Zhannura", "keep@example.test"));
        mvc.perform(put("/api/students/{id}", student.getId()).contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"\",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest());
        assertThat(repository.findById(student.getId()).orElseThrow().getEmail()).isEqualTo("keep@example.test");
    }

    @Test void duplicateEmailReturns409() throws Exception {
        repository.save(new Student("Existing", "unique@example.test"));
        mvc.perform(post("/api/students").contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Zhannura\",\"email\":\"unique@example.test\"}"))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.error").exists());
    }
}
