package dev.matheuslf.desafio.inscritos.dto.request;

import dev.matheuslf.desafio.inscritos.enums.StatusTask;

public record RequestUpdateTaskStatus(StatusTask newStatus) {
}
