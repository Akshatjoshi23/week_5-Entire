package com.example.spring_learning.controller;

import com.example.spring_learning.Student;
import com.example.spring_learning.dto.StudentDTO;
import com.example.spring_learning.dto.StudentPageDTO;
import com.example.spring_learning.dto.StudentRequestDTO;
import com.example.spring_learning.exception.StudentNotFoundException;
import com.example.spring_learning.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student CRUD and search operations")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "List students", description = "Returns a page of students; name filtering is case-insensitive.")
    @ApiResponse(responseCode = "200", description = "Student page returned")
    public ResponseEntity<StudentPageDTO> getAllStudents(
            @Parameter(description = "Zero-based page index") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size from 1 to 100") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Optional partial name filter") @RequestParam(required = false) String name) {
        StudentService.StudentPage result = studentService.findStudents(name, page, size);
        List<StudentDTO> students = result.students().stream()
                .map(student -> new StudentDTO(
                        student.getId(), student.getName(), student.getEmail()))
                .toList();
        return ResponseEntity.ok(new StudentPageDTO(
                students, result.page(), result.size(), result.totalElements(), result.totalPages()));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<Student>> getStudents(Pageable pageable) {
        return ResponseEntity.ok(studentService.getStudents(pageable));
    } 

    @GetMapping("/{id}")
    @Operation(summary = "Get a student", description = "Reads one student by its database-generated ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student returned"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
        public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        return ResponseEntity.ok(new StudentDTO(
                student.getId(), student.getName(), student.getEmail()));
    }

    @PostMapping
    @Operation(summary = "Create a student")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<StudentDTO> createStudent(
            @Valid @RequestBody StudentRequestDTO request) {
        Student student = studentService.createStudent(request.getName(), request.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new StudentDTO(student.getId(), student.getName(), student.getEmail()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a student")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated"),
            @ApiResponse(responseCode = "400", description = "Request validation failed"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Student not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<StudentDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequestDTO request) {
        Student student = studentService.updateStudent(
                id, request.getName(), request.getEmail());
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        return ResponseEntity.ok(new StudentDTO(
                student.getId(), student.getName(), student.getEmail()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a student")
    @SecurityRequirement(name = "basicAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
        public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentService.deleteStudent(id)) {
            throw new StudentNotFoundException(id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/hello")
    public String hello() {
        return "Student API is working!";
    }
} // <-- Added the class closing brace here
