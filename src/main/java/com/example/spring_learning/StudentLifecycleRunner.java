
package com.example.spring_learning;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentLifecycleRunner implements ApplicationRunner {

    private final StudentLifecycleService studentLifecycleService;

    public StudentLifecycleRunner(
            StudentLifecycleService studentLifecycleService) {

        this.studentLifecycleService = studentLifecycleService;
    }

    @Override
    public void run(ApplicationArguments args) {

        studentLifecycleService.displayStudentInfo();
    }
}
