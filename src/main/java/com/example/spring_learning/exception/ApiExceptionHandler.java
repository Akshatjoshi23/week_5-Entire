package com.example.spring_learning.exception;

import com.example.spring_learning.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            StudentNotFoundException exception,
            HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        Map<String, TreeSet<String>> messagesByField = new TreeMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
            messagesByField.computeIfAbsent(error.getField(), ignored -> new TreeSet<>())
                .add(error.getDefaultMessage()));
        Map<String, String> fieldErrors = new TreeMap<>();
        messagesByField.forEach((field, messages) ->
            fieldErrors.put(field, String.join("; ", messages)));
        return error(HttpStatus.BAD_REQUEST, "Validation failed", request, fieldErrors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleBadRequest(
            Exception exception,
            HttpServletRequest request) {
        String message = exception instanceof IllegalArgumentException
                ? exception.getMessage()
                : "Malformed request body";
        return error(HttpStatus.BAD_REQUEST, message, request, Map.of());
    }

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiError> handleConflict(
                        DataIntegrityViolationException exception,
                        HttpServletRequest request) {
                return error(HttpStatus.CONFLICT,
                                "A student with this email already exists", request, Map.of());
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception exception,
            HttpServletRequest request) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred", request, Map.of());
    }

    private ResponseEntity<ApiError> error(
            HttpStatus status,
            String message,
            HttpServletRequest request,
            Map<String, String> fieldErrors) {
        ApiError body = new ApiError(Instant.now(), status.value(),
                status.getReasonPhrase(), message, request.getRequestURI(), fieldErrors);
        return ResponseEntity.status(status).body(body);
    }
}