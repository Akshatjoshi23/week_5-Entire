
package com.example.spring_learning;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentArchiveRunner implements ApplicationRunner {

    private final StudentArchiveService archiveService;

    public StudentArchiveRunner(StudentArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    @Override
    public void run(ApplicationArguments args) {

        archiveService.archiveStudent();
    }
}
