package com.example.spring_learning.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.spring_learning.UserSessionInfo;

@RestController
@RequestMapping("/api/session-scope")
public class SessionScopeController {

    private final UserSessionInfo userSessionInfo;

    public SessionScopeController(UserSessionInfo userSessionInfo) {
        this.userSessionInfo = userSessionInfo;
    }

    @GetMapping
    public Map<String, Object> getSessionInfo() {
        int visits = userSessionInfo.increaseVisitCount();
        String username = userSessionInfo.getUsername();

        return Map.of(
            "message", "Session info retrieved successfully",
            "sessionId", userSessionInfo.getSessionId(),
            "username", username == null ? "Not provided" : username,
            "visitCount", visits
        );
    }

    @PostMapping("/username")
    public Map<String, String> setUsername(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        userSessionInfo.setUsername(username);
        return Map.of(
            "message", "Username saved successfully",
            "username", String.valueOf(userSessionInfo.getUsername())
        );
    }

    @PostMapping("/reset")
    public Map<String, String> resetVisitCount() {
        userSessionInfo.resetVisitCount();
        return Map.of(
            "message", "Visit count reset successfully",
            "visitCount", String.valueOf(userSessionInfo.getVisitCount())
        );
    }
}
