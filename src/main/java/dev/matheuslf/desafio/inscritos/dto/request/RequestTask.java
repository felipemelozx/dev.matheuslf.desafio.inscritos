package dev.matheuslf.desafio.inscritos.dto.request;


import dev.matheuslf.desafio.inscritos.enums.PriorityTask;
import dev.matheuslf.desafio.inscritos.enums.StatusTask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Date;

import jakarta.validation.constraints.*;

public record RequestTask(

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 5, max = 150, message = "O título deve ter entre 5 e 150 caracteres")
    String title,

    String description,

    @NotNull(message = "O status é obrigatório")
    StatusTask status,

    @NotNull(message = "A prioridade é obrigatória")
    PriorityTask priority,

    @FutureOrPresent(message = "A data limite deve ser futura ou o dia atual")
    Date dueDate,

    @NotNull(message = "O ID do projeto é obrigatório")
    Long projectId

) {}

