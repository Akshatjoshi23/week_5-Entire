package com.example.spring_learning;
import com.example.spring_learning.repository.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@Service 
public class StudentReportService {
    private StudentRepository studentRepository;
    @Autowired 
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    public int printStudentCount() {
        int count = studentRepository.findAll().size();
        System.out.println("Total number of students: " + count);
        return count;
    }
}
