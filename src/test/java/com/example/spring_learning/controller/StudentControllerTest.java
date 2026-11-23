package com.example.spring_learning.controller;

import com.example.spring_learning.exception.ApiExceptionHandler;
import com.example.spring_learning.Student;
import com.example.spring_learning.repository.StudentRepository;
import com.example.spring_learning.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StudentControllerTest {

    private MockMvc mockMvc;
        private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        studentRepository = mock(StudentRepository.class);
        when(studentRepository.findByNameContainingIgnoreCase(eq("rah"), any(Pageable.class)))
            .thenReturn(new PageImpl<>(List.of(new Student("Rahul", "rahul@example.com", null)),
                PageRequest.of(0, 1), 1));
        StudentService studentService = new StudentService(
            studentRepository, message -> { });
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new StudentController(studentService))
                .setControllerAdvice(new ApiExceptionHandler())
                .setValidator(validator)
                .build();
    }

    @Test
    void filtersAndPaginatesStudents() throws Exception {
        mockMvc.perform(get("/api/students")
                        .param("name", "rah")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Rahul"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(1))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    @Test
    void createsStudentAndReturnsPersistedRecord() throws Exception {
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
                Student persistedStudent = new Student(
                    student.getName(), student.getEmail(), student.getAge());
                ReflectionTestUtils.setField(persistedStudent, "id", 3L);
                return persistedStudent;
        });

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Taylor Reed\",\"email\":\"taylor@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Taylor Reed"))
                .andExpect(jsonPath("$.email").value("taylor@example.com"));
    }

    @Test
    void returnsStructuredErrorsForInvalidStudentRequest() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\" \",\"email\":\"invalid\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.path").value("/api/students"))
                .andExpect(jsonPath("$.fieldErrors.name").value(
                    "Name must contain 2 to 50 characters; Name must not be blank"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email must be valid"));
    }

    @Test
    void returnsStructuredErrorForInvalidPagination() throws Exception {
        mockMvc.perform(get("/api/students").param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Page must be zero or greater"))
                .andExpect(jsonPath("$.path").value("/api/students"));
    }

        @Test
        void rejectsPageSizesOutsideSupportedRange() throws Exception {
        mockMvc.perform(get("/api/students").param("size", "0"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Size must be between 1 and 100"));

        mockMvc.perform(get("/api/students").param("size", "101"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Size must be between 1 and 100"));
        }

        @Test
        void rejectsMalformedJsonAndNamesOutsideLengthBounds() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{not-json"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Malformed request body"));

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"A\",\"email\":\"a@example.com\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors.name").exists());

        String tooLongName = "A".repeat(51);
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + tooLongName
                    + "\",\"email\":\"a@example.com\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.fieldErrors.name").exists());
        }

        @Test
        void returnsNotFoundForMissingStudentOnReadUpdateAndDelete() throws Exception {
        when(studentRepository.findById(404L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/api/students/404"))
            .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/students/404")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Taylor Reed\",\"email\":\"taylor@example.com\"}"))
            .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/students/404"))
            .andExpect(status().isNotFound());
        }
}