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
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;

  public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository,
                     UserRepository userRepository) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
    this.projectRepository = projectRepository;
    this.userRepository = userRepository;
  }

  public ResponseTask create(RequestTask requestTask, UserModel currentUser) throws AccessDeniedException {
    ProjectModel project = projectRepository.findById(requestTask.projectId())
        .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

    if(project.getCreatedBy() != null && !currentUser.getId().equals(project.getCreatedBy().getId())){
      throw new AccessDeniedException("Você não tem autorização para criar task neste projeto.");
    }

    UserModel assignedUser = null;

    if (requestTask.assignedUserId() != null) {
      assignedUser = userRepository.findById(requestTask.assignedUserId())
          .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
    }

    TaskModel task = taskMapper.toTaskModel(requestTask, currentUser, assignedUser);
    TaskModel taskSave = taskRepository.save(task);
    return taskMapper.toResponseTask(taskSave);
  }

  public void deleteById(Long taskId, UserModel currentUser) throws AccessDeniedException {
    TaskModel taskModel = taskRepository.findById(taskId)
        .orElseThrow(() -> new EntityNotFoundException("Task não encontrada."));
    boolean isTheSameUserAssignee =  taskModel.getAssignee() != null &&
        currentUser.getId().equals(taskModel.getAssignee().getId());
    boolean isTheSameUserCreatedBy = currentUser.getId().equals(taskModel.getCreatedBy().getId());
    if(!isTheSameUserAssignee && !isTheSameUserCreatedBy){
      throw new AccessDeniedException("Você não tem autorização para apagar esta task");
    }
    taskRepository.deleteById(taskId);
  }

  public List<ResponseTask> findTasks(StatusTask status, PriorityTask priority, Long projectId) {
    if (status == null && priority == null && projectId == null) {
      return taskMapper.toResponseTask(taskRepository.findAll());
    }
    return taskMapper.toResponseTask(taskRepository.findAllByFilter(status, priority, projectId));
  }

  public ResponseTask updateStatus(Long taskId, RequestUpdateTaskStatus status, UserModel currentUser) throws AccessDeniedException {
    TaskModel taskModel = taskRepository.findById(taskId)
        .orElseThrow(() -> new EntityNotFoundException("Task não encontrada."));

    boolean isTheSameUserAssignee =  taskModel.getAssignee() != null &&
        currentUser.getId().equals(taskModel.getAssignee().getId());
    boolean isTheSameUserCreatedBy = currentUser.getId().equals(taskModel.getCreatedBy().getId());

    if(!isTheSameUserAssignee && !isTheSameUserCreatedBy) {
      throw new AccessDeniedException("Você não tem autorização para atualizar esta task.");
    }

    taskModel.setStatus(status.newStatus());
    return taskMapper.toResponseTask(taskRepository.save(taskModel));
  }
}
