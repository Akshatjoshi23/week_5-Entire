package com.example.spring_learning;

import org.springframework.stereotype.Component;

@Component 
public class StudentScanningDemo {
    public StudentScanningDemo(){
        System.out.println("StudentScanningDemo bean dscovered");

    }
    public void display(){
        System.out.println("Component scanning is working");
    }
}
