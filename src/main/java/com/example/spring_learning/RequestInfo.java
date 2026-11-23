package com.example.spring_learning;

import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
@Component
@RequestScope
public class RequestInfo {
    private final String requestId;

    public RequestInfo(){
        this.requestId=UUID.randomUUID().toString();
        System.out.println("New RequestInfo bean created: "+ requestId);

    }
    public String getRequestId(){
        return requestId;
    }
}
