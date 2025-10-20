package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.request.RequestLogin;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUser;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  private RequestUser userTest;

  @BeforeEach
  void setup() {
    userRepository.deleteAll();
    userTest = new RequestUser("Test User", "test@email.com", "Password123!");
  }

  @Test
  void shouldRegisterUserSuccessfully() throws Exception {
    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userTest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.email").value(userTest.email()))
        .andExpect(jsonPath("$.data.name").value(userTest.name()))
        .andExpect(jsonPath("$.message").value("Usuário criado com sucesso."))
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void shouldFailRegisterWithInvalidData() throws Exception {
    RequestUser invalidUser = new RequestUser("", "invalid-email", "");
    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidUser)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldLoginSuccessfully() throws Exception {
    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userTest)))
        .andExpect(status().isOk());

    RequestLogin loginRequest = new RequestLogin(userTest.email(), userTest.password());

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.accessToken", notNullValue()))
        .andExpect(jsonPath("$.message").value("Login realizado com sucesso."))
        .andExpect(jsonPath("$.success").value(true));
  }

  @Test
  void shouldFailLoginWithNonExistingUser() throws Exception {
    RequestLogin loginRequest = new RequestLogin("nonexistent@email.com", "anyPassword");

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isNotFound()) // 404
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message", containsString("não encontrado")));
  }

  @Test
  void shouldFailLoginWithWrongPassword() throws Exception {
    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userTest)))
        .andExpect(status().isOk());

    RequestLogin loginRequest = new RequestLogin(userTest.email(), "WrongPassword");

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isUnauthorized()) // 401
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message", containsString("Error ao tentar fazer login.")))
        .andExpect(jsonPath("$.errors[0].field").value("Password"));
  }

  @Test
  void shouldFailRegisterWhenEmailAlreadyExists() throws Exception {
    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userTest)))
        .andExpect(status().isOk());

    mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userTest)))
        .andExpect(status().isBadRequest()) // Deve cair no handler de DataIntegrityViolationException
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Erro de integridade no banco de dados."))
        .andExpect(jsonPath("$.errors[0].field").value("Email"))
        .andExpect(jsonPath("$.errors[0].message").value("Este e-mail já está cadastrado!"));
  }
}
