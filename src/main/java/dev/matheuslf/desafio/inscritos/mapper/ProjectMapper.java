package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import jakarta.validation.Valid;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {

  public ProjectModel toProjectModel(@Valid RequestProject requestBody) {
    return new ProjectModel()
        .setId(null)
        .setName(requestBody.name())
        .setDescription(requestBody.description())
        .setStartDate(requestBody.startDate())
        .setEndDate(requestBody.endDate());
  }

  public ResponseProject toResponseProject(ProjectModel projectModel) {
    return new ResponseProject(
        projectModel.getId(),
        projectModel.getName(),
        projectModel.getDescription(),
        projectModel.getStartDate(),
        projectModel.getEndDate()
    );
  }
}
