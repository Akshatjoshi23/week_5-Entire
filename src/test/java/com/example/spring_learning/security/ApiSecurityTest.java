package com.example.spring_learning.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:api-security-test;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.security.user.name=test-writer",
        "spring.security.user.password=test-password"
})
@AutoConfigureMockMvc
class ApiSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void allowsPublicReadRequests() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    void rejectsWriteRequestsWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Taylor Reed\",\"email\":\"taylor@example.com\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication required"));

        mockMvc.perform(put("/api/students/1"))
            .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/students/1"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void allowsAuthenticatedCreateRequests() throws Exception {
        mockMvc.perform(post("/api/students")
                        .with(httpBasic("test-writer", "test-password"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Taylor Reed\",\"email\":\"taylor@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("taylor@example.com"));
    }

            @Test
            void authenticatedCreateRejectsInvalidFieldsWithValidationDetails() throws Exception {
            mockMvc.perform(post("/api/students")
                    .with(httpBasic("test-writer", "test-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"A\",\"email\":\"not-an-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
            }

            @Test
            void duplicateEmailReturnsConflict() throws Exception {
            String requestBody = "{\"name\":\"Morgan Lee\",\"email\":\"duplicate@example.com\"}";

            mockMvc.perform(post("/api/students")
                    .with(httpBasic("test-writer", "test-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isCreated());

            mockMvc.perform(post("/api/students")
                    .with(httpBasic("test-writer", "test-password"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                    .value("A student with this email already exists"));
            }

            @Test
            void authenticatedCrudRoundTripPersistsUpdatesAndDeletes() throws Exception {
            String basicCredentials = "test-writer";
            String password = "test-password";
            String createBody = "{\"name\":\"Avery Quinn\",\"email\":\"avery@example.com\"}";

            String response = mockMvc.perform(post("/api/students")
                    .with(httpBasic(basicCredentials, password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(createBody))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
            long id = tools.jackson.databind.json.JsonMapper.builder().build()
                .readTree(response).get("id").asLong();

            mockMvc.perform(put("/api/students/{id}", id)
                    .with(httpBasic(basicCredentials, password))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"name\":\"Avery Lane\",\"email\":\"avery.lane@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Avery Lane"))
                .andExpect(jsonPath("$.email").value("avery.lane@example.com"));

            mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Avery Lane"));

            mockMvc.perform(delete("/api/students/{id}", id)
                    .with(httpBasic(basicCredentials, password)))
                .andExpect(status().isNoContent());

            mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
            }

    @Test
    void exposesOpenApiDocumentWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/students']").exists())
                .andExpect(jsonPath("$.components.securitySchemes.basicAuth.scheme").value("basic"))
                .andExpect(jsonPath("$.paths['/api/students'].post.security[0].basicAuth").exists());
    }
}