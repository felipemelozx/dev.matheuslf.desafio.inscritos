package dev.matheuslf.desafio.inscritos.dto.response;

import java.util.Date;

public record ResponseProject(
    Long id,
    String name,
    String description,
    Date startDate,
    Date endDate
) {}