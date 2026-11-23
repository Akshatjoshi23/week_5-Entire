
package com.example.spring_learning.service;

import com.example.spring_learning.Student;
import com.example.spring_learning.repository.StudentRepository;
import com.example.spring_learning.notification.NotificationService;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    public StudentService(
            StudentRepository studentRepository,
            @Qualifier("emailNotificationService")
            NotificationService notificationService) {

        this.studentRepository = studentRepository;
        this.notificationService = notificationService;
    }
    public void registerStudent() {
       notificationService.sendNotification("Student has been registered successfully.");
    }

    // Get all students
    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public StudentPage findStudents(String name, int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page must be zero or greater");
        }
        if (size < 1 || size > 100) {
            throw new IllegalArgumentException("Size must be between 1 and 100");
        }

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        Page<Student> result = name == null || name.isBlank()
                ? studentRepository.findAll(pageable)
                : studentRepository.findByNameContainingIgnoreCase(name.trim(), pageable);

        return new StudentPage(result.getContent(), result.getNumber(), result.getSize(),
                result.getTotalElements(), result.getTotalPages());
    }

        public record StudentPage(
            List<Student> students,
            int page,
            int size,
            long totalElements,
            int totalPages) {
        }

    // Get a student by ID
    public Student getStudentById(Long id) {

        return studentRepository.findById(id).orElse(null);
    }

    // Create a new student
    public Student createStudent(String name, String email) {

        Student student = studentRepository.save(new Student(name, email, null));

        notificationService.sendNotification(
                "Student " + name +
                " has been registered successfully."
        );

        return student;
    }
    
    // Update an existing student
    public Student updateStudent(Long id, String name, String email) {

        Student student = getStudentById(id);

        if (student == null) {
            return null;
        }

        student.setName(name);
        student.setEmail(email);
        return studentRepository.save(student);
    }

    // Delete a student
    public boolean deleteStudent(Long id) {

        Student student = getStudentById(id);

        if (student == null) {
            return false;
        }

        studentRepository.delete(student);

        return true;
    }
    public Page<Student> getStudents(Pageable pageable) {
    return studentRepository.findAll(pageable);
}
}
