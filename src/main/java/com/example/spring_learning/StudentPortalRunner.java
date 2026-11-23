
package com.example.spring_learning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StudentPortalRunner implements CommandLineRunner {

    private final StudentPortalSetting settings;
    private final String notificationMessage;

    public StudentPortalRunner(
            StudentPortalSetting settings,
            String notificationMessage) {

        this.settings = settings;
        this.notificationMessage = notificationMessage;
    }

    @Override
    public void run(String... args) {
        System.out.println("----- STUDENT PORTAL -----");

        settings.displaySettings();

        System.out.println(notificationMessage);
    }
}
