
package com.example.spring_learning.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_learning.ApplicationInfo;

@RestController
@RequestMapping("/api/application-scope")
public class ApplicationScopeController {

    private final ApplicationInfo applicationInfo;

    public ApplicationScopeController(ApplicationInfo applicationInfo) {
        this.applicationInfo = applicationInfo;
    }

    @GetMapping
    public Map<String, Object> getApplicationInfo() {

        int currentVisits = applicationInfo.recordVisit();

        return Map.of(
            "message", "Application Scope Demonstration",
            "applicationId", applicationInfo.getApplicationId(),
            "totalVisits", currentVisits
        );
    }
}
