
package com.example.spring_learning.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentDTO {

    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 50, message = "Name must contain 2 to 50 characters")
    private String name;

    private String email;

    public StudentDTO() {
    }

    public StudentDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public StudentDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
