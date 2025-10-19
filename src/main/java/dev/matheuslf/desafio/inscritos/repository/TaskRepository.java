package dev.matheuslf.desafio.inscritos.repository;

import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import dev.matheuslf.desafio.inscritos.model.TaskModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long> {

  @Query("""
    SELECT t FROM TaskModel t
    WHERE (:status IS NULL OR t.status = :status)
      AND (:priority IS NULL OR t.priority = :priority)
      AND (:projectId IS NULL OR t.project.id = :projectId)
  """)
  List<TaskModel> findAllByFilter(@Param("status") StatusTask status,
                                     @Param("priority") PriorityTask priority,
                                     @Param("projectId") Long projectId);
}
