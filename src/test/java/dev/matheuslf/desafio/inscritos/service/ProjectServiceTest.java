package dev.matheuslf.desafio.inscritos.service;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.mapper.ProjectMapper;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import dev.matheuslf.desafio.inscritos.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private ProjectMapper projectMapper;
  @InjectMocks
  private ProjectService projectService;


  @Test
  void shouldCreateProjectSuccessfully() {
    UserModel userModel = new UserModel().setId(1l);
    RequestProject request = new RequestProject("Projeto A", "Descrição A", null, null);
    ProjectModel projectModel = new ProjectModel(null, "Projeto A", "Descrição A", null, null, userModel);
    ProjectModel savedProject = new ProjectModel(1L, "Projeto A", "Descrição A", null, null);
    ResponseProject response = new ResponseProject(1L, "Projeto A", userModel.getId(),"Descrição A", null, null);

    when(projectMapper.toProjectModel(request, userModel)).thenReturn(projectModel);
    when(projectRepository.save(projectModel)).thenReturn(savedProject);
    when(projectMapper.toResponseProject(savedProject)).thenReturn(response);

    ResponseProject result = projectService.create(request, userModel);

    assertNotNull(result);
    assertEquals(1L, result.id());
    assertEquals(1L, result.createdBy());
    verify(projectRepository, times(1)).save(projectModel);
    verify(projectMapper, times(1)).toResponseProject(savedProject);
  }

  @Test
  void shouldReturnAllProjects() {
    UserModel userModel = new UserModel().setId(1l);
    ProjectModel project1 = new ProjectModel(1L, "Projeto A", "Desc", null, null, userModel);
    ProjectModel project2 = new ProjectModel(2L, "Projeto B", "Desc B", null, null, userModel);

    ResponseProject response1 = new ResponseProject(1L, "Projeto A", userModel.getId(), "Desc", null, null);
    ResponseProject response2 = new ResponseProject(2L, "Projeto B", userModel.getId(), "Desc B", null, null);

    when(projectRepository.findAll()).thenReturn(List.of(project1, project2));
    when(projectMapper.toResponseProject(List.of(project1, project2))).thenReturn(List.of(response1, response2));

    List<ResponseProject> results = projectService.getAllProjects();

    assertEquals(2, results.size());
    verify(projectRepository, times(1)).findAll();
    verify(projectMapper, times(1)).toResponseProject(List.of(project1, project2));
  }
}
