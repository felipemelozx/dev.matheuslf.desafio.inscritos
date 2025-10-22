package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import dev.matheuslf.desafio.inscritos.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ResponseTask>> create(@RequestBody @Valid RequestTask requestTask,
                                                          @AuthenticationPrincipal UserModel user) throws AccessDeniedException {
    ResponseTask data = taskService.create(requestTask, user);
    var body = ApiResponse.<ResponseTask>success()
        .message("Task criada com sucesso")
        .data(data)
        .build();
    return ResponseEntity.ok().body(body);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ResponseTask>>> getTasks(
      @RequestParam(required = false) StatusTask status,
      @RequestParam(required = false) PriorityTask priority,
      @RequestParam(required = false) Long projectId) {

    var data = taskService.findTasks(status, priority, projectId);
    var body = ApiResponse.<List<ResponseTask>>success()
        .message("Sucesso ao buscar todas as Tasks")
        .data(data)
        .build();
    return ResponseEntity.ok().body(body);
  }

  @PutMapping("/{taskId}/status")
  public ResponseEntity<ApiResponse<ResponseTask>> updateStatus(@PathVariable Long taskId,
                                                                @RequestBody RequestUpdateTaskStatus status,
                                                                @AuthenticationPrincipal UserModel currentUser) throws AccessDeniedException {

    var data = taskService.updateStatus(taskId, status, currentUser);
    var body = ApiResponse.<ResponseTask>success()
        .message("Atualização realizada com sucesso")
        .data(data)
        .build();
    return ResponseEntity.ok().body(body);
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> deleteById(@PathVariable Long taskId,
                                         @AuthenticationPrincipal UserModel currentUser) throws AccessDeniedException {
    taskService.deleteById(taskId, currentUser);
    return ResponseEntity.noContent().build();
  }
}
