package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDTO(

        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @NotBlank(message = "A senha não pode estar em branco")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres por motivos de segurança")
        String senha,

        @NotNull(message = "O perfil de acesso (Role) deve ser informado (ex: ROLE_USER)")
        UserRole role
) {}