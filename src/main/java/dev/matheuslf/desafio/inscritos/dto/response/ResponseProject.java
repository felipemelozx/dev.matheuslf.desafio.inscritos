package dev.matheuslf.desafio.inscritos.dto.response;

import java.util.Date;

public record ResponseProject(
    Long id,
    String name,
    Long createdBy,
    String description,
    Date startDate,
    Date endDate
) {}