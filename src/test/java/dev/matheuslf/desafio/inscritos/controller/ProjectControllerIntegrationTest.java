package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProjectRepository projectRepository;

  @BeforeEach
  void setup() {
    projectRepository.deleteAll();
  }

  @Test
  void shouldCreateProjectSuccessfully() throws Exception {
    RequestProject request = new RequestProject("Projeto Teste", "Descrição Teste", new Date(), new Date());

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("Projeto Teste"))
        .andExpect(jsonPath("$.data.description").value("Descrição Teste"))
        .andExpect(jsonPath("$.data.id").isNumber());
  }

  @Test
  void shouldReturnAllProjects() throws Exception {
    ProjectModel project = new ProjectModel()
        .setName("Projeto 1")
        .setDescription("Desc")
        .setStartDate(new Date())
        .setEndDate(new Date());
    projectRepository.save(project);

    mockMvc.perform(get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()").value(1))
        .andExpect(jsonPath("$.data[0].name").value("Projeto 1"));
  }

  @Test
  void shouldReturnEmptyListWhenNoProjects() throws Exception {
    mockMvc.perform(get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()", is(0)));
  }

  @Test
  void shouldFailCreateProjectWithMissingName() throws Exception {
    RequestProject request = new RequestProject(null, "Descrição", new Date(), new Date());

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldFailCreateProjectWithMissingDates() throws Exception {
    RequestProject request = new RequestProject("Projeto X", "Descrição", null, null);

    mockMvc.perform(post("/projects")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }
}
