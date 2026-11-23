package com.example.spring_learning;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component 
@Scope("prototype")
public class StudentCertificate {
    public StudentCertificate(){
        System.out.println("New certificate created!");
    }
    public void generateCertificate(){
        System.out.println("Certificate generated for student.");
    }
}
