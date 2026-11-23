
package com.example.spring_learning;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;

@Component
@ApplicationScope
public class ApplicationInfo {

    private final String applicationId;
    private final AtomicInteger totalVisits;

    public ApplicationInfo() {
        this.applicationId = UUID.randomUUID().toString();
        this.totalVisits = new AtomicInteger(0);

        System.out.println("Application Scope bean created!");
    }

    public int recordVisit() {
        return totalVisits.incrementAndGet();
    }

    public String getApplicationId() {
        return applicationId;
    }

    public int getTotalVisits() {
        return totalVisits.get();
    }
}
