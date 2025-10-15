package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final ProjectMapper projectMapper;

  public ProjectService(ProjectRepository projectRepository, ProjectMapper projectMapper) {
    this.projectRepository = projectRepository;
    this.projectMapper = projectMapper;
  }

  @Transactional
  public ResponseProject create(@Valid RequestProject requestBody) {
    ProjectModel project = projectMapper.toProjectModel(requestBody);
    ProjectModel projectSave = projectRepository.save(project);
    return projectMapper.toResponseProject(projectSave);
  }

  public List<ResponseProject> getAllProjects() {
    List<ProjectModel> projectModelList = projectRepository.findAll();
    return projectMapper.toResponseProject(projectModelList);
  }
}
