package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.AccessDeniedException;
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

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private TaskService taskService;

  private UserModel creator;
  private ProjectModel project;

  @BeforeEach
  void setup() {
    creator = createUser(1L, "Matheus");
    project = createProject(1L, creator);
  }

  private UserModel createUser(Long id, String name) {
    return new UserModel(id, name, name.toLowerCase() + "@email.com", "somePassword");
  }

  private ProjectModel createProject(Long id, UserModel creator) {
    return new ProjectModel(id, "Projeto " + id, "Descrição " + id, null, null, creator);
  }

  private TaskModel createTask(Long id, ProjectModel project, UserModel createdBy, UserModel assignee, StatusTask status, PriorityTask priority) {
    return new TaskModel(id, "Tarefa " + id, "Desc " + id, status, priority, null, project, createdBy, assignee);
  }

  @Test
  void create_shouldThrowAccessDenied_whenUserIsNotProjectCreator() {
    UserModel anotherCreator = createUser(99L, "Outro");
    ProjectModel otherProject = createProject(2L, anotherCreator);

    RequestTask request = new RequestTask(
        "Tarefa 1", "Descrição", StatusTask.TODO, PriorityTask.HIGH, null, 2L, null
    );

    when(projectRepository.findById(2L)).thenReturn(Optional.of(otherProject));

    AccessDeniedException exception = assertThrows(AccessDeniedException.class,
        () -> taskService.create(request, creator));

    assertEquals("Você não tem autorização para criar task neste projeto.", exception.getMessage());
  }

  @Test
  void create_shouldSucceed_whenProjectExistsAndAssignedUserIsFound() throws AccessDeniedException {
    RequestTask request = new RequestTask(
        "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, 1L, 2L
    );
    UserModel assignedUser = createUser(2L, "João");
    TaskModel taskModel = createTask(null, project, creator, assignedUser, StatusTask.TODO, PriorityTask.HIGH);
    TaskModel savedTask = createTask(1L, project, creator, assignedUser, StatusTask.TODO, PriorityTask.HIGH);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc", 1L, StatusTask.TODO, PriorityTask.HIGH, creator.getId(), assignedUser.getId(), null);

    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(userRepository.findById(2L)).thenReturn(Optional.of(assignedUser));
    when(taskMapper.toTaskModel(request, creator, assignedUser)).thenReturn(taskModel);
    when(taskRepository.save(taskModel)).thenReturn(savedTask);
    when(taskMapper.toResponseTask(savedTask)).thenReturn(response);

    ResponseTask result = taskService.create(request, creator);

    assertEquals(1L, result.taskId());
    verify(taskRepository).save(taskModel);
  }

  @Test
  void create_shouldThrowEntityNotFound_whenProjectDoesNotExist() {
    RequestTask request = new RequestTask("Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, 1L, null);

    when(projectRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> taskService.create(request, creator));
  }

  @Test
  void create_shouldThrowEntityNotFound_whenAssignedUserDoesNotExist() {
    RequestTask request = new RequestTask("Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, null, 1L, 99L);

    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(userRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> taskService.create(request, creator));
  }

  @Test
  void create_shouldSucceed_withoutAssignedUser() throws AccessDeniedException {
    RequestTask request = new RequestTask("Tarefa sem atribuição", "Descrição", StatusTask.TODO, PriorityTask.MEDIUM, null, 1L, null);

    TaskModel taskModel = createTask(null, project, creator, null, StatusTask.TODO, PriorityTask.MEDIUM);
    TaskModel savedTask = createTask(1L, project, creator, null, StatusTask.TODO, PriorityTask.MEDIUM);
    ResponseTask response = new ResponseTask(1L, "Tarefa sem atribuição", "Descrição", 1L, StatusTask.TODO, PriorityTask.MEDIUM, creator.getId(), null, null);

    when(projectRepository.findById(1L)).thenReturn(Optional.of(project));
    when(taskMapper.toTaskModel(request, creator, null)).thenReturn(taskModel);
    when(taskRepository.save(taskModel)).thenReturn(savedTask);
    when(taskMapper.toResponseTask(savedTask)).thenReturn(response);

    ResponseTask result = taskService.create(request, creator);

    assertEquals(1L, result.taskId());
    assertEquals(StatusTask.TODO, result.status());
    assertNull(result.assignedByUserId());

    verify(taskRepository).save(taskModel);
    verifyNoInteractions(userRepository);
  }

  @Test
  void deleteById_shouldSucceed_whenUserIsCreatorOrAssignee() throws AccessDeniedException {
    UserModel currentUser = createUser(1L, "Matheus");
    TaskModel task = createTask(1L, project, currentUser, currentUser, StatusTask.TODO, PriorityTask.HIGH);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

    taskService.deleteById(1L, currentUser);

    verify(taskRepository).deleteById(1L);
  }

  @Test
  void deleteById_shouldThrowAccessDenied_whenUserIsNotCreatorOrAssignee() {
    UserModel creator = createUser(100L, "Creator");
    UserModel assignee = createUser(200L, "Assignee");
    TaskModel task = createTask(1L, project, creator, assignee, StatusTask.TODO, PriorityTask.HIGH);
    UserModel anotherUser = createUser(300L, "Other");

    when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

    assertThrows(AccessDeniedException.class, () -> taskService.deleteById(1L, anotherUser));
    verify(taskRepository, never()).deleteById(anyLong());
  }

  @Test
  void updateStatus_shouldSucceed_whenUserIsCreatorOrAssignee() throws AccessDeniedException {
    UserModel currentUser = createUser(1L, "Matheus");
    TaskModel existingTask = createTask(1L, project, currentUser, currentUser, StatusTask.TODO, PriorityTask.HIGH);
    TaskModel updatedTask = createTask(1L, project, currentUser, currentUser, StatusTask.DONE, PriorityTask.HIGH);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc 1", null, StatusTask.DONE, PriorityTask.HIGH, null, null, null);

    when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
    existingTask.setStatus(StatusTask.DONE);
    when(taskRepository.save(existingTask)).thenReturn(updatedTask);
    when(taskMapper.toResponseTask(updatedTask)).thenReturn(response);

    ResponseTask result = taskService.updateStatus(1L, new RequestUpdateTaskStatus(StatusTask.DONE), currentUser);

    assertEquals(StatusTask.DONE, result.status());
  }

  @Test
  void updateStatus_shouldThrowAccessDenied_whenUserIsNotCreatorOrAssignee() {
    UserModel creator = createUser(100L, "Creator");
    UserModel assignee = createUser(200L, "Assignee");
    TaskModel taskModel = createTask(1L, project, creator, assignee, StatusTask.TODO, PriorityTask.HIGH);
    UserModel anotherUser = createUser(300L, "Other");

    when(taskRepository.findById(1L)).thenReturn(Optional.of(taskModel));

    assertThrows(AccessDeniedException.class, () -> taskService.updateStatus(1L, new RequestUpdateTaskStatus(StatusTask.DONE), anotherUser));
    verify(taskRepository, never()).save(any());
  }

  @Test
  void updateStatus_shouldThrowEntityNotFound_whenTaskDoesNotExist() {
    UserModel user = createUser(10L, "User");

    when(taskRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> taskService.updateStatus(1L, new RequestUpdateTaskStatus(StatusTask.DONE), user));
  }


  @Test
  void findTasks_shouldReturnAll_whenNoFilters() {
    TaskModel task1 = createTask(1L, project, null, null, StatusTask.TODO, PriorityTask.HIGH);
    TaskModel task2 = createTask(2L, project, null, null, StatusTask.DOING, PriorityTask.LOW);
    ResponseTask response1 = new ResponseTask(1L, "Tarefa 1", "Desc 1", 1L, StatusTask.TODO, PriorityTask.HIGH, null, null, null);
    ResponseTask response2 = new ResponseTask(2L, "Tarefa 2", "Desc 2", 1L, StatusTask.DOING, PriorityTask.LOW, null, null, null);

    when(taskRepository.findAll()).thenReturn(List.of(task1, task2));
    when(taskMapper.toResponseTask(List.of(task1, task2))).thenReturn(List.of(response1, response2));

    List<ResponseTask> results = taskService.findTasks(null, null, null);

    assertEquals(2, results.size());
    assertTrue(results.contains(response1));
    assertTrue(results.contains(response2));
  }

  @Test
  void findTasks_shouldReturnFilteredTasks() {
    TaskModel task = createTask(1L, project, null, null, StatusTask.TODO, PriorityTask.HIGH);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc 1", 1L, StatusTask.TODO, PriorityTask.HIGH, null, null, null);

    when(taskRepository.findAllByFilter(StatusTask.TODO, PriorityTask.HIGH, 1L)).thenReturn(List.of(task));
    when(taskMapper.toResponseTask(List.of(task))).thenReturn(List.of(response));

    List<ResponseTask> results = taskService.findTasks(StatusTask.TODO, PriorityTask.HIGH, 1L);

    assertEquals(1, results.size());
    assertEquals(response, results.get(0));
  }

  @Test
  void findTasks_shouldReturnTasksWithOnlyProjectFilter() {
    TaskModel task = createTask(1L, project, null, null, StatusTask.TODO, PriorityTask.HIGH);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc 1", 1L, StatusTask.TODO, PriorityTask.HIGH, null, null, null);

    when(taskRepository.findAllByFilter(null, null, 1L)).thenReturn(List.of(task));
    when(taskMapper.toResponseTask(List.of(task))).thenReturn(List.of(response));

    List<ResponseTask> results = taskService.findTasks(null, null, 1L);

    assertEquals(1, results.size());
    assertEquals(response, results.get(0));
  }

  @Test
  void findTasks_shouldReturnTasksWithOnlyPriorityFilter() {
    TaskModel task = createTask(1L, project, null, null, StatusTask.TODO, PriorityTask.HIGH);
    ResponseTask response = new ResponseTask(1L, "Tarefa 1", "Desc 1", 1L, StatusTask.TODO, PriorityTask.HIGH, null, null, null);

    when(taskRepository.findAllByFilter(null, PriorityTask.HIGH, null)).thenReturn(List.of(task));
    when(taskMapper.toResponseTask(List.of(task))).thenReturn(List.of(response));

    List<ResponseTask> results = taskService.findTasks(null, PriorityTask.HIGH, null);

    assertEquals(1, results.size());
    assertEquals(response, results.get(0));
  }
}