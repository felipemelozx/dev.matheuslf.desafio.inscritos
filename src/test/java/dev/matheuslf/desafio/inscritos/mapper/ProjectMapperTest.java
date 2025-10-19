package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestProject;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseProject;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectMapperTest {

    private ProjectMapper projectMapper;

    @BeforeEach
    void setUp() {
        projectMapper = new ProjectMapper();
    }

    @Test
    void shouldMapRequestProjectToProjectModel() {
        RequestProject request = new RequestProject("Projeto A", "Descrição", new Date(), new Date());

        ProjectModel projectModel = projectMapper.toProjectModel(request);

        assertNull(projectModel.getId());
        assertEquals("Projeto A", projectModel.getName());
        assertEquals("Descrição", projectModel.getDescription());
        assertNotNull(projectModel.getStartDate());
        assertNotNull(projectModel.getEndDate());
    }

    @Test
    void shouldMapProjectModelToResponseProject() {
        ProjectModel project = new ProjectModel(1L, "Projeto A", "Desc", new Date(), new Date());

        ResponseProject response = projectMapper.toResponseProject(project);

        assertEquals(1L, response.id());
        assertEquals("Projeto A", response.name());
        assertEquals("Desc", response.description());
        assertNotNull(response.startDate());
        assertNotNull(response.endDate());
    }

    @Test
    void shouldMapListOfProjectModelToResponseProjectList() {
        ProjectModel project1 = new ProjectModel(1L, "Projeto A", "Desc", new Date(), new Date());
        ProjectModel project2 = new ProjectModel(2L, "Projeto B", "Desc B", new Date(), new Date());

        List<ResponseProject> responses = projectMapper.toResponseProject(List.of(project1, project2));

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals(2L, responses.get(1).id());
    }
}
