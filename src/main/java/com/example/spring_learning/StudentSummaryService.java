package com.example.spring_learning;

import com.example.spring_learning.repository.StudentRepository;

public class StudentSummaryService {
    private final StudentRepository studentRepository;

    public StudentSummaryService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public void printStudentCount() {
        System.out.println("Student Summary:");
        studentRepository.findAll().forEach(student -> {
            System.out.println("ID: " + student.getId() + ", Name: " + student.getName());
        });
    }

}
