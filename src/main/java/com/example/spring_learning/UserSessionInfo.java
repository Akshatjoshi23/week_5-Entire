package com.example.spring_learning;

import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSessionInfo {

    private String username;
    private final String sessionId;
    private int visitCount;

    public UserSessionInfo() {
        this.sessionId = UUID.randomUUID().toString();
        this.visitCount = 0;
        System.out.println("New user session bean created");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public int increaseVisitCount() {
        visitCount++;
        return visitCount;
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void resetVisitCount() {
        visitCount = 0;
    }

    public String messageExceed() {
        if (visitCount > 5) {
            resetVisitCount();
            return "You are a regular visitor!";
        }
        return "Welcome! keep exploring";
    }
}
