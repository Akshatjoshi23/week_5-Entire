package com.example.spring_learning.dto;

import java.util.List;

public record StudentPageDTO(
        List<StudentDTO> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {
}