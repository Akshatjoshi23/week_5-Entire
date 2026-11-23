package com.example.spring_learning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StudentPropertyService {

    @Value("${student.portal.name}")
    private String portalName;

    @Value("${student.portal.max-students}")
    private int maxStudents;

    @Value("${student.portal.active}")
    private boolean active;

    public void displayProperties() {
        System.out.println("Portal Name: " + portalName);
        System.out.println("Maximum Students: " + maxStudents);
        System.out.println("Portal Active: " + active);
    }
}