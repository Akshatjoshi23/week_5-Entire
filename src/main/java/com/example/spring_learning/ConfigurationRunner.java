package com.example.spring_learning;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationRunner implements CommandLineRunner {

    private final StudentPropertyService propertyService;
    private final StudentPortalProperties properties;

    public ConfigurationRunner(
            StudentPropertyService propertyService,
            StudentPortalProperties properties) {
        this.propertyService = propertyService;
        this.properties = properties;
    }

    @Override
    public void run(String... args) {

        System.out.println("\n--- USING @VALUE ---");
        propertyService.displayProperties();

        System.out.println("\n--- USING @ConfigurationProperties ---");
        properties.display();
    }
}