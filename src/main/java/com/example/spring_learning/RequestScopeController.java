package com.example.spring_learning;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/request-scope")
public class RequestScopeController {

    private final RequestInfo requestInfo;

    public RequestScopeController(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
    }

    @GetMapping
    public Map<String, String> getRequestInfo() {
        return Map.of(
            "message", "Request Scope Demonstration",
            "requestId", requestInfo.getRequestId()
        );
    }
}