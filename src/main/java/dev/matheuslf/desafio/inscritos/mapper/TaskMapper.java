package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TaskMapper {
  public TaskModel toTaskModel(@Valid RequestTask requestTask, UserModel userCreatedBy, UserModel assignee) {
    return new TaskModel()
        .setId(null)
        .setTitle(requestTask.title())
        .setDescription(requestTask.description())
        .setDueDate(requestTask.dueDate())
        .setCreatedBy(userCreatedBy)
        .setAssignee(assignee)
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
        task.getCreatedBy().getId(),
        (task.getAssignee() == null ? null : task.getAssignee().getId()),
        task.getDueDate()
    );
  }

  public List<ResponseTask> toResponseTask(List<TaskModel> listTask) {
    return listTask.stream()
        .map(this::toResponseTask)
        .toList();
  }
}
