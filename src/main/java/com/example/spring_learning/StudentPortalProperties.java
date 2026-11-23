package com.example.spring_learning;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "student.portal")
public class StudentPortalProperties {

    private String name;
    private int maxStudents;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(int maxStudents) {
        this.maxStudents = maxStudents;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void display() {
        System.out.println("Name: " + name);
        System.out.println("Max Students: " + maxStudents);
        System.out.println("Active: " + active);
    }
}