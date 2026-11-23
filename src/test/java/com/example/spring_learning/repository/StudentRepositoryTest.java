package com.example.spring_learning.repository;

import com.example.spring_learning.Student;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void persistsStudentCrudAndSupportsNameFiltering() {
        Student created = studentRepository.saveAndFlush(
                new Student("Jordan Lee", "jordan@example.com", null));
        Long id = created.getId();
        assertThat(id).isPositive();

        entityManager.clear();
        Student read = studentRepository.findById(id).orElseThrow();
        assertThat(read.getName()).isEqualTo("Jordan Lee");
        assertThat(read.getEmail()).isEqualTo("jordan@example.com");

        read.setName("Jordan Smith");
        studentRepository.saveAndFlush(read);
        entityManager.clear();
        assertThat(studentRepository.findById(id).orElseThrow().getName())
                .isEqualTo("Jordan Smith");

        Page<Student> matches = studentRepository.findByNameContainingIgnoreCase(
                "smith", PageRequest.of(0, 10));
        assertThat(matches.getContent()).extracting(Student::getId).containsExactly(id);

        studentRepository.deleteById(id);
        assertThat(studentRepository.findById(id)).isEmpty();
    }
}