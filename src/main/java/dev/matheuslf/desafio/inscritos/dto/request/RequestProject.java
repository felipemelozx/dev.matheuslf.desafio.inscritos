package dev.matheuslf.desafio.inscritos.dto.request;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

import java.util.Date;


public record RequestProject(@NotBlank
                             @Length(min = 3, max = 100)
                             String name,
                             String description,
                             Date startDate,
                             Date endDate) {
}
