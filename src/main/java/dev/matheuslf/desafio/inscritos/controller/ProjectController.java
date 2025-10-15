package dev.matheuslf.desafio.inscritos.controller;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.service.ProjectService;
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
  public ResponseEntity<ResponseProject> create(@RequestBody @Valid RequestProject requestBody){
    ResponseProject body = projectService.create(requestBody);
    return ResponseEntity.ok().body(body);
  }

  @GetMapping
  public ResponseEntity<List<ResponseProject>> getAllProjects(){
    List<ResponseProject> response = projectService.getAllProjects();
    return ResponseEntity.ok().body(response);
  }

}
