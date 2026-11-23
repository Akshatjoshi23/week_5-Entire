package com.example.spring_learning;

import com.example.spring_learning.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentDataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;

    public StudentDataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            studentRepository.save(new Student("Akshat", "akshat@example.com", null));
            studentRepository.save(new Student("Rahul", "rahul@example.com", null));
        }
    }
}