package dev.matheuslf.desafio.inscritos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
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
class TaskControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private TaskRepository taskRepository;

  private ProjectModel project;

  @BeforeEach
  void setup() {
    taskRepository.deleteAll();
    projectRepository.deleteAll();

    project = new ProjectModel()
        .setName("Projeto Teste")
        .setDescription("Projeto para testes")
        .setStartDate(new Date())
        .setEndDate(new Date());
    project = projectRepository.save(project);
  }

  @Test
  void shouldReturnBadRequestWhenBodyIsMissing() throws Exception {
    mockMvc.perform(put("/tasks/1/status")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Required request body is missing"));
  }

  @Test
  void shouldReturnNotFoundWhenProjectNotFound() throws Exception {
    RequestTask request = new RequestTask(
        "Tarefa 1",
        "Descrição tarefa",
        StatusTask.TODO,
        PriorityTask.HIGH,
        new Date(),
        9999L
    );

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("Projeto não encontrado"))
        .andExpect(jsonPath("$.data").isEmpty())
        .andExpect(jsonPath("$.errors").isEmpty())
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void shouldCreateTaskSuccessfully() throws Exception {

    RequestTask request = new RequestTask(
        "Tarefa 1",
        "Descrição tarefa",
        StatusTask.TODO,
        PriorityTask.HIGH,
        new Date(),
        project.getId()
    );

    mockMvc.perform(post("/tasks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Task criada com sucesso"))
        .andExpect(jsonPath("$.data.title").value("Tarefa 1"))
        .andExpect(jsonPath("$.data.projectId").value(project.getId()))
        .andExpect(jsonPath("$.data.status").value("TODO"))
        .andExpect(jsonPath("$.data.priority").value("HIGH"));
  }

  @Test
  void shouldReturnAllTasks() throws Exception {
    TaskModel task1 = new TaskModel()
        .setTitle("Tarefa 1")
        .setDescription("Desc 1")
        .setStatus(StatusTask.TODO)
        .setPriority(PriorityTask.HIGH)
        .setProject(project)
        .setDueDate(new Date());
    TaskModel task2 = new TaskModel()
        .setTitle("Tarefa 2")
        .setDescription("Desc 2")
        .setStatus(StatusTask.DOING)
        .setPriority(PriorityTask.LOW)
        .setProject(project)
        .setDueDate(new Date());

    taskRepository.save(task1);
    taskRepository.save(task2);

    mockMvc.perform(get("/tasks"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()", is(2)))
        .andExpect(jsonPath("$.data[0].title", is("Tarefa 1")))
        .andExpect(jsonPath("$.data[1].title", is("Tarefa 2")));
  }

  @Test
  void shouldFilterTasksByStatusPriorityAndProject() throws Exception {
    TaskModel task = new TaskModel()
        .setTitle("Tarefa Filtrada")
        .setDescription("Desc")
        .setStatus(StatusTask.TODO)
        .setPriority(PriorityTask.HIGH)
        .setProject(project)
        .setDueDate(new Date());

    taskRepository.save(task);

    mockMvc.perform(get("/tasks")
            .param("status", "TODO")
            .param("priority", "HIGH")
            .param("projectId", project.getId().toString()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.length()", is(1)))
        .andExpect(jsonPath("$.data[0].title", is("Tarefa Filtrada")));
  }

  @Test
  void shouldUpdateTaskStatus() throws Exception {
    TaskModel task = new TaskModel()
        .setTitle("Tarefa Atualizar")
        .setDescription("Desc")
        .setStatus(StatusTask.TODO)
        .setPriority(PriorityTask.HIGH)
        .setProject(project)
        .setDueDate(new Date());

    task = taskRepository.save(task);

    RequestUpdateTaskStatus updateStatus = new RequestUpdateTaskStatus(StatusTask.DONE);

    mockMvc.perform(put("/tasks/{taskId}/status", task.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(updateStatus)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.status", is("DONE")));
  }

  @Test
  void shouldDeleteTask() throws Exception {
    TaskModel task = new TaskModel()
        .setTitle("Tarefa Deletar")
        .setDescription("Desc")
        .setStatus(StatusTask.TODO)
        .setPriority(PriorityTask.HIGH)
        .setProject(project)
        .setDueDate(new Date());

    task = taskRepository.save(task);

    mockMvc.perform(delete("/tasks/{taskId}", task.getId()))
        .andExpect(status().isNoContent());
  }
}
