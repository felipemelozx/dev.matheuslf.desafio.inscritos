package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerIntegrationTest extends BaseIntegrationTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void shouldCreateAndReturnTaskSuccessfully() throws Exception {
    RequestTask request = new RequestTask(
        "Tarefa 1",
        "Descrição 1",
        StatusTask.TODO,
        PriorityTask.HIGH,
        new Date(),
        projectId,
        userModelTest.getId()
    );

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.title").value("Tarefa 1"))
        .andExpect(jsonPath("$.data.status").value("TODO"))
        .andExpect(jsonPath("$.data.priority").value("HIGH"))
        .andExpect(jsonPath("$.data.projectId").value(projectId));
  }

  @Test
  void shouldFilterTasksByStatusPriorityAndProject() throws Exception {
    taskRepository.deleteAll();
    RequestTask request = new RequestTask(
        "Tarefa Filtrada",
        "Descrição teste",
        StatusTask.TODO,
        PriorityTask.HIGH,
        new Date(),
        projectId,
        userModelTest.getId()
    );

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk());

    mockMvc.perform(get("/tasks")
            .param("status", "TODO")
            .param("priority", "HIGH")
            .param("projectId", projectId.toString())
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data", hasSize(1)))
        .andExpect(jsonPath("$.data[0].title").value("Tarefa Filtrada"));
  }

  @Test
  void shouldUpdateTaskStatusSuccessfully() throws Exception {
    RequestTask createRequest = new RequestTask(
        "Tarefa Atualizar",
        "Desc",
        StatusTask.TODO,
        PriorityTask.HIGH,
        new Date(),
        projectId,
        userModelTest.getId()
    );

    var createResult = mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andReturn();

    Long taskId = objectMapper
        .readTree(createResult.getResponse().getContentAsString())
        .get("data").get("taskId").asLong();

    RequestUpdateTaskStatus updateRequest = new RequestUpdateTaskStatus(StatusTask.DONE);

    mockMvc.perform(put("/tasks/{id}/status", taskId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateRequest))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status").value("DONE"));
  }

  @Test
  void shouldDeleteTaskSuccessfully() throws Exception {
    RequestTask createRequest = new RequestTask(
        "some title",
        "some",
        StatusTask.DOING,
        PriorityTask.HIGH,
        new Date(),
        projectId,
        null
    );
    var result = mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(createRequest))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isOk())
        .andReturn();

    Long taskId = objectMapper
        .readTree(result.getResponse().getContentAsString())
        .get("data").get("taskId").asLong();

    mockMvc.perform(delete("/tasks/{id}", taskId)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNoContent());
  }

  @Test
  void shouldReturnBadRequestWhenMissingBodyOnUpdate() throws Exception {
    mockMvc.perform(put("/tasks/1/status")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Invalid or missing request body"));
  }

  @Test
  void shouldFailToCreateTaskWithInvalidProject() throws Exception {
    RequestTask invalid = new RequestTask(
        "Tarefa Inválida",
        "Projeto inexistente",
        StatusTask.TODO,
        PriorityTask.LOW,
        new Date(),
        9999L,
        userModelTest.getId()
    );

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(invalid))
            .header("Authorization", "Bearer " + accessToken))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Projeto não encontrado"));
  }
}
