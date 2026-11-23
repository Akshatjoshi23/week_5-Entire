package com.example.spring_learning.notification;

public class WhatsApNotificationService implements NotificationService {
    @Override
    public void sendNotification(String message) {
        System.out.println("WhatsApp Notification: " + message);
    }

}
