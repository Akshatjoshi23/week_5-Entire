package com.example.spring_learning.exception;

public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(Long id) {
        super("Student with id " + id + " was not found");
    }
}