package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.UserRole;

public record RegisterDTO(String email, String senha, UserRole role) {}
