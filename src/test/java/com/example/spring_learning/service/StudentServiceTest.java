package com.example.spring_learning.service;

import com.example.spring_learning.Student;
import com.example.spring_learning.notification.NotificationService;
import com.example.spring_learning.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private NotificationService notificationService;

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService(studentRepository, notificationService);
    }

    @Test
    void rejectsPageAndSizeOutsideSupportedBounds() {
        assertThatThrownBy(() -> studentService.findStudents(null, -1, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Page must be zero or greater");
        assertThatThrownBy(() -> studentService.findStudents(null, 0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Size must be between 1 and 100");
        assertThatThrownBy(() -> studentService.findStudents(null, 0, 101))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Size must be between 1 and 100");

        verify(studentRepository, never()).findAll(any(PageRequest.class));
    }

    @Test
    void usesPagedListForNullOrBlankName() {
        when(studentRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(), PageRequest.of(0, 10), 0));

        StudentService.StudentPage result = studentService.findStudents("  ", 0, 10);

        assertThat(result.students()).isEmpty();
        assertThat(result.page()).isZero();
        assertThat(result.size()).isEqualTo(10);
        verify(studentRepository).findAll(any(PageRequest.class));
        verify(studentRepository, never())
                .findByNameContainingIgnoreCase(any(), any(PageRequest.class));
    }

    @Test
    void trimsNameFilterAndReturnsPageMetadata() {
        Student student = new Student("Jordan Lee", "jordan@example.com", null);
        when(studentRepository.findByNameContainingIgnoreCase(eq("Jordan"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(student), PageRequest.of(1, 2), 3));

        StudentService.StudentPage result = studentService.findStudents(" Jordan ", 1, 2);

        assertThat(result.students()).containsExactly(student);
        assertThat(result.page()).isEqualTo(1);
        assertThat(result.totalElements()).isEqualTo(3);
        assertThat(result.totalPages()).isEqualTo(2);
    }

    @Test
    void savesStudentBeforeSendingRegistrationNotification() {
        Student savedStudent = new Student("Jordan Lee", "jordan@example.com", null);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        Student result = studentService.createStudent("Jordan Lee", "jordan@example.com");

        assertThat(result).isSameAs(savedStudent);
        var order = org.mockito.Mockito.inOrder(studentRepository, notificationService);
        order.verify(studentRepository).save(any(Student.class));
        order.verify(notificationService).sendNotification(
                "Student Jordan Lee has been registered successfully.");
    }

    @Test
    void doesNotSaveOrDeleteWhenStudentDoesNotExist() {
        when(studentRepository.findById(404L)).thenReturn(Optional.empty());

        assertThat(studentService.updateStudent(404L, "Jordan Lee", "jordan@example.com"))
                .isNull();
        assertThat(studentService.deleteStudent(404L)).isFalse();

        verify(studentRepository, never()).save(any(Student.class));
        verify(studentRepository, never()).delete(any(Student.class));
    }
}