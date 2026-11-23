package com.example.spring_learning.mapper;

import com.example.spring_learning.Student;
import com.example.spring_learning.dto.StudentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponseDTO toResponseDTO(Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
    }
}