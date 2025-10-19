package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.mapper.TaskMapper;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import dev.matheuslf.desafio.inscritos.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskMapper taskMapper;
  private final ProjectRepository projectRepository;

  public TaskService(TaskRepository taskRepository, TaskMapper taskMapper, ProjectRepository projectRepository) {
    this.taskRepository = taskRepository;
    this.taskMapper = taskMapper;
    this.projectRepository = projectRepository;
  }

  public ResponseTask create(@Valid RequestTask requestTask) {
    projectRepository.findById(requestTask.projectId())
        .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado"));

    TaskModel task = taskMapper.toTaskModel(requestTask);
    TaskModel taskSave = taskRepository.save(task);
    return taskMapper.toResponseTask(taskSave);
  }

  public void deleteById(Long taskId) {
    taskRepository.deleteById(taskId);
  }

  public List<ResponseTask> findTasks(StatusTask status, PriorityTask priority, Long projectId) {
    if (status == null && priority == null && projectId == null) {
      return taskRepository.findAll().stream()
          .map(taskMapper::toResponseTask)
          .toList();
    }
   return taskMapper.toResponseTask(taskRepository.findAllByFilter(status, priority, projectId));
  }

  public ResponseTask updateStatus(Long taskId, RequestUpdateTaskStatus status) {
    TaskModel taskModel = taskRepository.findById(taskId)
        .orElseThrow(() -> new EntityNotFoundException("Task not found."));

    return taskMapper.toResponseTask(taskRepository.save(taskModel.setStatus(status.newStatus())));
  }
}
