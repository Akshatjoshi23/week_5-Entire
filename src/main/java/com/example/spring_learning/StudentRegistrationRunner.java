
package com.example.spring_learning;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentRegistrationRunner implements ApplicationRunner {

    private final StudentRegistrationService registrationService;

    public StudentRegistrationRunner(
            StudentRegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public void run(ApplicationArguments args) {
        registrationService.registerStudent();
    }
}
