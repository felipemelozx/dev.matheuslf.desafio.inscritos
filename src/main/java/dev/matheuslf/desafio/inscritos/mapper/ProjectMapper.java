package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectMapper {

  public ProjectModel toProjectModel(@Valid RequestProject requestBody, UserModel user) {
    return new ProjectModel()
        .setId(null)
        .setName(requestBody.name())
        .setDescription(requestBody.description())
        .setStartDate(requestBody.startDate())
        .setEndDate(requestBody.endDate())
        .setCreatedBy(user);
  }

  public ResponseProject toResponseProject(ProjectModel projectModel) {
    return new ResponseProject(
        projectModel.getId(),
        projectModel.getName(),
        projectModel.getCreatedBy().getId(),
        projectModel.getDescription(),
        projectModel.getStartDate(),
        projectModel.getEndDate()
    );
  }

  public List<ResponseProject> toResponseProject(List<ProjectModel> projectList) {
    return projectList.stream()
        .map(this::toResponseProject)
        .toList();
  }
}
