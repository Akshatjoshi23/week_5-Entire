package com.example.spring_learning;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.spring_learning.notification.NotificationService;

@Service 
public class StudentSmsService{
    private final NotificationService notificationService;
    public StudentSmsService(@Qualifier ("smsNotificationService") NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    public void sendStudentMessage(){
        notificationService.sendNotification("Student has been registered successfully.");
    }
}
