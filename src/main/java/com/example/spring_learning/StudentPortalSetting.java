package com.example.spring_learning;

public class StudentPortalSetting {
    private final String portalName;
    private final int maximumStudents;

    public StudentPortalSetting(String portalName,int maximumStudents){
        this.portalName=portalName;
        this.maximumStudents=maximumStudents;
    }
    public void displaySettings(){
        System.out.println("Portal Name: "+portalName);
        System.out.println("Maximum Students: "+maximumStudents);
    }

}
