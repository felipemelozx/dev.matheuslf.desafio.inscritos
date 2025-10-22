package dev.matheuslf.desafio.inscritos.dto.response;

import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;

import java.util.Date;

public record ResponseTask(Long taskId,
                           String title,
                           String description,
                           Long projectId,
                           StatusTask status,
                           PriorityTask priority,
                           Long createdByUserId,
                           Long assignedByUserId,
                           Date dueDate) {
}
