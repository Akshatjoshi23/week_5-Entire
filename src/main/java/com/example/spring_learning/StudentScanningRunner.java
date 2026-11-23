package com.example.spring_learning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component 
public class StudentScanningRunner implements CommandLineRunner{
    private final StudentScanningDemo demo;
    public StudentScanningRunner(StudentScanningDemo demo){
        this.demo=demo;
    }
    @Override 
    public void run(String... args){
        demo.display();
    }

}
