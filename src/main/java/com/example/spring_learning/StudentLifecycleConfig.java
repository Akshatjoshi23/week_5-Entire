
package com.example.spring_learning;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StudentLifecycleConfig {

    @Bean(
        initMethod = "startService",
        destroyMethod = "stopService"
    )
    public StudentArchiveService studentArchiveService() {

        return new StudentArchiveService();
    }
}
