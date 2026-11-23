package com.example.spring_learning.notification;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.*;

@Component
@Primary 
public class EmailNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("Email notification sent with message: " + message);
    }
    
}
