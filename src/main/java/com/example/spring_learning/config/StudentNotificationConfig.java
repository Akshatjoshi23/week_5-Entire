
package com.example.spring_learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentNotificationConfig {

    @Bean
    public String studentNotificationMessage() {
        return "Student notification system is ready!";
    }
}
