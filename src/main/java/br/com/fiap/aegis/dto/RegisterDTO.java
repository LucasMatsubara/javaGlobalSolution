package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterDTO(
        @NotBlank(message = "O e-mail é obrigatório para a criação da conta")
        @Email(message = "O formato do e-mail inserido é inválido")
        String email,

        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve conter no mínimo 6 caracteres por motivos de segurança")
        String senha,

        @NotNull(message = "O perfil de acesso (Role) é obrigatório (ex: ROLE_USER, ROLE_ADMIN)")
        UserRole role
) {}