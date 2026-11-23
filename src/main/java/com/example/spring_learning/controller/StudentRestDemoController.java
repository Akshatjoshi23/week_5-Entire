package com.example.spring_learning.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/demo/students")
public class StudentRestDemoController {

    private final List<String> students = new ArrayList<>(
            List.of("Rahul", "Priya", "Amit")
    );

    // GET: Retrieve all students
    @GetMapping
    public ResponseEntity<List<String>> getAllStudents() {
        return ResponseEntity.ok(students);
    }

    // GET: Retrieve one student by index
    @GetMapping("/{id}")
    public ResponseEntity<String> getStudent(@PathVariable int id) {

        if (id < 0 || id >= students.size()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(students.get(id));
    }

    // GET: Search using query parameter
    @GetMapping("/search")
    public ResponseEntity<String> searchStudent(
            @RequestParam String name) {

        return students.stream()
                .filter(student -> student.equalsIgnoreCase(name))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Add a student
    @PostMapping
    public ResponseEntity<String> addStudent(
            @RequestBody String name) {

        students.add(name);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Student added: " + name);
    }

    // PUT: Update student
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(
            @PathVariable int id,
            @RequestBody String name) {

        if (id < 0 || id >= students.size()) {
            return ResponseEntity.notFound().build();
        }

        students.set(id, name);

        return ResponseEntity.ok("Student updated: " + name);
    }

    // DELETE: Remove student
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(
            @PathVariable int id) {

        if (id < 0 || id >= students.size()) {
            return ResponseEntity.notFound().build();
        }

        students.remove(id);

        return ResponseEntity.noContent().build();
    }
}
