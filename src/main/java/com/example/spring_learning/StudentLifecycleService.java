package com.example.spring_learning;
import jakarta.annotation.*;
import org.springframework.stereotype.Component;
@Component 
public class StudentLifecycleService {
    public StudentLifecycleService(){
        System.out.println("1. Constructor: StudentLifecycleService object created!");

    }
    @PostConstruct 
    public void initialize(){
        System.out.println("2. PostConstruct: Student service initialized!");
    }
    public void displayStudentInfo(){
        System.out.println("3. Bean is ready: Displaying student information!");
    }
    @PreDestroy 
    public void cleanup(){
        System.out.println("4. PreDestroy: Cleaning up StudentLifecycleService!");
    }
}
