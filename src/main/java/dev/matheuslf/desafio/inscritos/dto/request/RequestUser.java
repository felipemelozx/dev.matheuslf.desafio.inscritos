package dev.matheuslf.desafio.inscritos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RequestUser(
    @NotBlank(message = "O nome é obrigatório.")
    String name,

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "O email está em um formato incorreto. Exemplo: algo@email.com")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Length(min = 6, message = "A senha deve ter no mínimo 6 caracteres.")
    String password
) {}
