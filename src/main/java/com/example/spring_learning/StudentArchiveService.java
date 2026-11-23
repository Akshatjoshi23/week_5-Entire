
package com.example.spring_learning;

public class StudentArchiveService {
    boolean serviceRunning;

    public StudentArchiveService() {
        System.out.println("1. Archive service constructor called");
    }

    public void startService() {
        serviceRunning=true;
        validateArchive();
        System.out.println("2. Archive service started successfully!");
    }

    public void archiveStudent() {
        System.out.println("Student archived successfully!");
    }

    public void stopService() {
        serviceRunning=false;
        System.out.println("3. Archive service stopped successfully!");
    }
    public void validateArchive(){
        System.out.println("Archive configuration validated");
    }
}
