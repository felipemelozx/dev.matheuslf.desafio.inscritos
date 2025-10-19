package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
import dev.matheuslf.desafio.inscritos.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService){
    this.projectService = projectService;
  }

  @PostMapping
  public ResponseEntity<ApiResponse<ResponseProject>> create(@RequestBody @Valid RequestProject requestBody){
    ResponseProject data = projectService.create(requestBody);
    var body = ApiResponse.<ResponseProject>success()
        .message("Projeto criado com sucesso")
        .data(data)
        .build();
    return ResponseEntity.ok().body(body);
  }

  @GetMapping
  public ResponseEntity<ApiResponse<List<ResponseProject>>> getAllProjects(){
    List<ResponseProject> data = projectService.getAllProjects();
    var body = ApiResponse.<List<ResponseProject>>success()
        .message("Sucesso ao buscar todos os projetos")
        .data(data)
        .build();
    return ResponseEntity.ok().body(body);
  }
}
