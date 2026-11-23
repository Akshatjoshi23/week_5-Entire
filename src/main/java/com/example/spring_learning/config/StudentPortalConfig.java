
package com.example.spring_learning.config;

import com.example.spring_learning.StudentPortalSetting;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(StudentNotificationConfig.class)
public class StudentPortalConfig {

    @Bean
    public StudentPortalSetting studentPortalSettings() {
        return new StudentPortalSetting(
                "Student Management Portal",
                500
        );
    }
}
