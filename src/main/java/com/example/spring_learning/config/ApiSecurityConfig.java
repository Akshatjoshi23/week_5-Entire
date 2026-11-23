package com.example.spring_learning.config;

import com.example.spring_learning.dto.ApiError;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Configuration
@SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic")
public class ApiSecurityConfig {

    @Bean
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity http, ObjectMapper objectMapper)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/students/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/students/**")
                        .hasRole("STUDENT_WRITER")
                        .requestMatchers(HttpMethod.PUT, "/api/students/**")
                        .hasRole("STUDENT_WRITER")
                        .requestMatchers(HttpMethod.DELETE, "/api/students/**")
                        .hasRole("STUDENT_WRITER")
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, exception) ->
                                writeSecurityError(request, response, objectMapper,
                                        HttpStatus.UNAUTHORIZED, "Authentication required"))
                        .accessDeniedHandler((request, response, exception) ->
                                writeSecurityError(request, response, objectMapper,
                                        HttpStatus.FORBIDDEN, "Access denied")));
        return http.build();
    }

    private void writeSecurityError(
            HttpServletRequest request,
            HttpServletResponse response,
            ObjectMapper objectMapper,
            HttpStatus status,
            String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), new ApiError(
                Instant.now(), status.value(), status.getReasonPhrase(), message,
                request.getRequestURI(), Map.of()));
    }
}