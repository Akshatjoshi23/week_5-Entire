package com.example.spring_learning;
import org.springframework.beans.factory.InitializingBean;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
@Component 
public class StudentRegistrationService implements 
InitializingBean,DisposableBean{

    int studentCount;

    public StudentRegistrationService(){
        System.out.println("1.Constructor: Registration service created");
    }
    @Override 
    public void afterPropertiesSet(){
        studentCount=0;
        System.out.println("2. Initialization: Registration service initialized");
    }
     public void registerStudent() {
        studentCount=studentCount+1;
        System.out.println(studentCount);
        System.out.println("3. Student registered successfully!");
    }

    @Override
    public void destroy() {
        System.out.println("4. DisposableBean: Registration service destroyed");
    }

}
