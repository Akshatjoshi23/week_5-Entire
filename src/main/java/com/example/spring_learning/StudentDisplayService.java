package com.example.spring_learning;
import org.springframework.stereotype.Service;
import com.example.spring_learning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
@Service
public class StudentDisplayService {
    private StudentRepository studentRepository;
    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    public void displayStudentCount(){
        int count = studentRepository.findAll().size();
        System.out.println("Total number of students: " + count);
    }

}
