package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.UserRole;

public record UsuarioResponseDTO(
        Long id,
        String email,
        UserRole role
) {}