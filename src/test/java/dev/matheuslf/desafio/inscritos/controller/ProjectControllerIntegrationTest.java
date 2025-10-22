package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldCreateProjectSuccessfully() throws Exception {
    RequestProject request = new RequestProject("Projeto Teste", "Descrição Teste", new Date(), new Date());

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("Projeto Teste"))
        .andExpect(jsonPath("$.data.description").value("Descrição Teste"))
        .andExpect(jsonPath("$.data.id").isNumber());
  }

  @Test
  void shouldReturnAllProjects() throws Exception {
    projectRepository.deleteAll();
    RequestProject project1 = new RequestProject("Projeto 1", "Desc 1", new Date(), new Date());
    RequestProject project2 = new RequestProject("Projeto 2", "Desc 2", new Date(), new Date());

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(project1))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(project2))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());

    mockMvc.perform(get("/projects")
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(2)))
        .andExpect(jsonPath("$.data[0].name", is("Projeto 1")))
        .andExpect(jsonPath("$.data[1].name", is("Projeto 2")));
  }

  @Test
  void shouldReturnEmptyListWhenNoProjects() throws Exception {
    projectRepository.deleteAll();
    mockMvc.perform(get("/projects")
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(0)));
  }

  @Test
  void shouldFailCreateProjectWithMissingName() throws Exception {
    RequestProject invalidRequest = new RequestProject(null, "Descrição inválida", new Date(), new Date());

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalidRequest))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", containsString("Validation failed")));
  }
}
