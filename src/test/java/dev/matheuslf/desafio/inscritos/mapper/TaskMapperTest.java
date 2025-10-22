package dev.matheuslf.desafio.inscritos.mapper;

import dev.matheuslf.desafio.inscritos.dto.request.RequestTask;
import dev.matheuslf.desafio.inscritos.dto.response.ResponseTask;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.model.ProjectModel;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import dev.matheuslf.desafio.inscritos.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    void shouldMapRequestTaskToTaskModel() {
        RequestTask request = new RequestTask("Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, new Date(), 1L, 1l);

        TaskModel taskModel = taskMapper.toTaskModel(request, new UserModel(), new UserModel());

        assertNull(taskModel.getId());
        assertEquals("Tarefa 1", taskModel.getTitle());
        assertEquals("Desc", taskModel.getDescription());
        assertEquals(StatusTask.TODO, taskModel.getStatus());
        assertEquals(PriorityTask.HIGH, taskModel.getPriority());
        assertNotNull(taskModel.getProject());
        assertEquals(1L, taskModel.getProject().getId());
    }

    @Test
    void shouldMapTaskModelToResponseTask() {
        ProjectModel project = new ProjectModel().setId(1L);
      UserModel user = new UserModel().setId(1l);

        TaskModel taskModel = new TaskModel(2L, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, new Date(), project, user, user);

        ResponseTask response = taskMapper.toResponseTask(taskModel);

        assertEquals(2L, response.taskId());
        assertEquals("Tarefa 1", response.title());
        assertEquals("Desc", response.description());
        assertEquals(1L, response.projectId());
        assertEquals(StatusTask.TODO, response.status());
        assertEquals(PriorityTask.HIGH, response.priority());
        assertNotNull(response.dueDate());
    }

    @Test
    void shouldMapListOfTaskModelToResponseTaskList() {
        ProjectModel project = new ProjectModel().setId(1L);
        UserModel user = new UserModel().setId(1l);
        TaskModel task1 = new TaskModel(1L, "Tarefa 1", "Desc", StatusTask.TODO, PriorityTask.HIGH, new Date(), project, user, user);
        TaskModel task2 = new TaskModel(2L, "Tarefa 2", "Desc 2", StatusTask.DOING, PriorityTask.LOW, new Date(), project, user, user);

        List<ResponseTask> responses = taskMapper.toResponseTask(List.of(task1, task2));

        assertEquals(2, responses.size());
        assertEquals(1L, responses.get(0).taskId());
        assertEquals(2L, responses.get(1).taskId());
    }
}
