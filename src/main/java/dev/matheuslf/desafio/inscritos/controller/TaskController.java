package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.request.RequestUpdateTaskStatus;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<ResponseTask> create(@RequestBody @Valid RequestTask requestTask){
    ResponseTask task = taskService.create(requestTask);
    return ResponseEntity.ok().body(task);
  }

  @GetMapping
  public ResponseEntity<List<ResponseTask>> getTasks(
      @RequestParam(required = false) StatusTask status,
      @RequestParam(required = false) PriorityTask priority,
      @RequestParam(required = false) Long projectId) {

    var body = taskService.findTasks(status, priority, projectId);
    return ResponseEntity.ok().body(body);
  }

  @PutMapping("/{taskId}/status")
  public ResponseEntity<ResponseTask> updateStatus(@PathVariable
                                           Long taskId,
                                           @RequestBody
                                           RequestUpdateTaskStatus status) {

    var response = taskService.updateStatus(taskId, status);
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/{taskId}")
  public ResponseEntity<Void> deleteById(@PathVariable Long taskId) {
    taskService.deleteById(taskId);
    return ResponseEntity.noContent().build();
  }
}
