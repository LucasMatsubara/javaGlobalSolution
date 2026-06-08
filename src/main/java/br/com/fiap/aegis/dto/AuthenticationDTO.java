package br.com.fiap.aegis.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O e-mail é obrigatório para realizar o login")
        @Email(message = "O formato do e-mail inserido é inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória para realizar o login")
        String senha
) {}