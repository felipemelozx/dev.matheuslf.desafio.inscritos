package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;
  @Mock
  private TaskMapper taskMapper;
  @Mock
  private ProjectRepository projectRepository;
  @InjectMocks
  private TaskService taskService;

  @Test
  void shouldCreateTaskSuccessfully() {
    RequestTask request = new RequestTask("Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, 1L);
    ProjectModel project = new ProjectModel(1L, "Projeto A", "Desc", null, null);
    TaskModel taskModel = new TaskModel(null, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, project);
    TaskModel savedTask = new TaskModel(1L, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, project);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc", 1L, StatusTask.TODO, PriorityTask.HIGH, null);

    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(taskMapper.toTaskModel(request)).thenReturn(taskModel);
    when(taskRepository.save(taskModel)).thenReturn(savedTask);
    when(taskMapper.toResponseTask(savedTask)).thenReturn(response);

    ResponseTask result = taskService.create(request);

    assertNotNull(result);
    assertEquals(1L, result.taskId());
    verify(taskRepository).save(taskModel);
  }

  @Test
  void shouldThrowExceptionWhenProjectNotFound() {
    RequestTask request = new RequestTask("Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, 1L);
    when(projectRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> taskService.create(request));
  }

  @Test
  void shouldDeleteTaskById() {
    taskService.deleteById(1L);
    verify(taskRepository, times(1)).deleteById(1L);
  }

  @Test
  void shouldUpdateTaskStatus() {
    TaskModel existingTask = new TaskModel(1L, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, null);
    TaskModel updatedTask = new TaskModel(1L, "Tarefa 1", "Desc", StatusTask.DONE, PriorityTask.HIGH, null, null);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc", 1L,StatusTask.DONE, PriorityTask.HIGH, null);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
    when(taskRepository.save(existingTask.setStatus(StatusTask.DONE))).thenReturn(updatedTask);
    when(taskMapper.toResponseTask(updatedTask)).thenReturn(response);

    ResponseTask result = taskService.updateStatus(1L, new RequestUpdateTaskStatus(StatusTask.DONE));

    assertEquals(StatusTask.DONE, result.status());
    verify(taskRepository).save(existingTask);
  }

  @Test
  void shouldReturnAllTasksWhenNoFilters() {
    ProjectModel project = new ProjectModel(1L, "Projeto A", "Desc", null, null);
    TaskModel task1 = new TaskModel(1L, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, project);
    TaskModel task2 = new TaskModel(2L, "Tarefa 2", "Desc", StatusTask.DOING, PriorityTask.LOW, null, project);

    ResponseTask response1 = new ResponseTask(1L, "Tarefa 1", "Desc", 1L, StatusTask.TODO, PriorityTask.HIGH, null);
    ResponseTask response2 = new ResponseTask(2L, "Tarefa 2", "Desc", 1L, StatusTask.DOING, PriorityTask.LOW, null);

    when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
    when(taskMapper.toResponseTask(List.of(task1, task2))).thenReturn(List.of(response1, response2));

    List<ResponseTask> results = taskService.findTasks(null, null, null);

    assertEquals(2, results.size());
    assertTrue(results.contains(response1));
    assertTrue(results.contains(response2));
    verify(taskRepository, times(1)).findAll();
    verify(taskMapper, times(1)).toResponseTask(List.of(task1, task2));
  }

  @Test
  void shouldReturnTasksWithFilters() {
    ProjectModel project = new ProjectModel(1L, "Projeto A", "Desc", null, null);
    TaskModel task1 = new TaskModel(1L, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, project);

    ResponseTask response1 = new ResponseTask(1L, "Tarefa 1", "Desc", 1L, StatusTask.TODO, PriorityTask.HIGH, null);

    when(taskRepository.findAllByFilter(StatusTask.TODO, PriorityTask.HIGH, 1L))
        .thenReturn(List.of(task1));
    when(taskMapper.toResponseTask(List.of(task1))).thenReturn(List.of(response1));

    List<ResponseTask> results = taskService.findTasks(StatusTask.TODO, PriorityTask.HIGH, 1L);

    assertEquals(1, results.size());
    assertEquals(response1, results.get(0));
    verify(taskRepository, times(1)).findAllByFilter(StatusTask.TODO, PriorityTask.HIGH, 1L);
    verify(taskMapper, times(1)).toResponseTask(List.of(task1));
  }

  @Test
  void shouldReturnTasksWithOnlyProjectFilter() {
    ProjectModel project = new ProjectModel(1L, "Projeto A", "Desc", null, null);
    TaskModel task1 = new TaskModel(1L, "Tarefa 3", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, project);
    ResponseTask response1 = new ResponseTask(1L, "Tarefa 3", "Desc", 1L, StatusTask.TODO, PriorityTask.HIGH, null);

    when(taskRepository.findAllByFilter(null, null, 1L)).thenReturn(List.of(task1));
    when(taskMapper.toResponseTask(List.of(task1))).thenReturn(List.of(response1));

    List<ResponseTask> results = taskService.findTasks(null, null, 1L);

    assertEquals(1, results.size());
    assertEquals(response1, results.get(0));
    verify(taskRepository, times(1)).findAllByFilter(null, null, 1L);
    verify(taskMapper, times(1)).toResponseTask(List.of(task1));
  }

  @Test
  void shouldReturnTasksWithOnlyPriorityFilter() {
    ProjectModel project = new ProjectModel(1L, "Projeto A", "Desc", null, null);
    TaskModel task1 = new TaskModel(1L, "Tarefa 3", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, project);
    ResponseTask response1 = new ResponseTask(1L, "Tarefa 3", "Desc", 1L, StatusTask.TODO, PriorityTask.HIGH, null);

    when(taskRepository.findAllByFilter(null, PriorityTask.HIGH, null)).thenReturn(List.of(task1));
    when(taskMapper.toResponseTask(List.of(task1))).thenReturn(List.of(response1));

    List<ResponseTask> results = taskService.findTasks(null, PriorityTask.HIGH, null);

    assertEquals(1, results.size());
    assertEquals(response1, results.get(0));
    verify(taskRepository, times(1)).findAllByFilter(null, PriorityTask.HIGH,null);
    verify(taskMapper, times(1)).toResponseTask(List.of(task1));
  }
}
