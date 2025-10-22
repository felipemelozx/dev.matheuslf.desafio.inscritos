package dev.matheuslf.desafio.inscritos.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import jakarta.validation.constraints.*;

import java.util.Date;

public record RequestTask(

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres")
    String title,

    @NotBlank(message = "A descrição é obrigatória")
    String description,

    @NotNull(message = "O status é obrigatório")
    StatusTask status,

    @NotNull(message = "A prioridade é obrigatória")
    PriorityTask priority,

    @JsonFormat(pattern = "yyyy-MM-dd")
    Date dueDate,

    @NotNull(message = "O ID do projeto é obrigatório")
    Long projectId,

    Long assignedUserId
) {}
