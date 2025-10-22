package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.request.RequestLogin;
import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUser;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
// 🔹 Isso permite usar @BeforeAll não-estático
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest {

  @Autowired protected MockMvc mockMvc;
  @Autowired protected ObjectMapper objectMapper;
  @Autowired protected UserRepository userRepository;
  @Autowired protected ProjectRepository projectRepository;
  @Autowired protected TaskRepository taskRepository;

  protected String accessToken;
  protected RequestUser userTest;
  protected UserModel userModelTest;
  protected RequestProject projectRequest;
  protected Long projectId;

  // 🔹 Executa uma única vez para toda a classe de teste
  @BeforeAll
  void globalSetup() throws Exception {
    taskRepository.deleteAll();
    projectRepository.deleteAll();
    userRepository.deleteAll();

    userTest = new RequestUser("Integration Test User", "integration@test.com", "Password123!");
    projectRequest = new RequestProject("Projeto Mockado", "Descrição via MockMvc", new Date(), new Date());

    // 🔸 Registra usuário
    var registerResponse = mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userTest)))
        .andExpect(status().isOk())
        .andReturn();

    var dataNode = objectMapper.readTree(registerResponse.getResponse().getContentAsString()).get("data");
    userModelTest = objectMapper.treeToValue(dataNode, UserModel.class);

    // 🔸 Faz login e salva o token
    var loginRequest = new RequestLogin(userTest.email(), userTest.password());
    var loginResult = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginRequest)))
        .andExpect(status().isOk())
        .andReturn();

    accessToken = objectMapper.readTree(loginResult.getResponse().getContentAsString())
        .get("data").get("accessToken").asText();

    var result = mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(projectRequest))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andReturn();

    JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
    projectId = json.get("data").get("id").asLong();
  }
}
