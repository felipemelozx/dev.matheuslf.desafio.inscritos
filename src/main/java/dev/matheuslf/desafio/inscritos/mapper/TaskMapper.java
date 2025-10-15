package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
  public TaskModel toTaskModel(@Valid RequestTask requestTask) {
    return new TaskModel()
        .setId(null)
        .setTitle(requestTask.title())
        .setDescription(requestTask.description())
        .setDueDate(requestTask.dueDate())
        .setPriority(requestTask.priority())
        .setStatus(requestTask.status())
        .setProject(new ProjectModel().setId(requestTask.projectId()));
  }

  public ResponseTask toResponseTask(TaskModel task) {
    return new ResponseTask(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        task.getProject().getId(),
        task.getStatus(),
        task.getPriority(),
        task.getDueDate()
    );
  }
}
