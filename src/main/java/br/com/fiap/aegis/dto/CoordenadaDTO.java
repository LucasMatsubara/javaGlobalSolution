package br.com.fiap.aegis.dto;

import jakarta.validation.constraints.NotNull;

public record CoordenadaDTO(
        @NotNull(message = "O eixo X é obrigatório") Double eixoX,
        @NotNull(message = "O eixo Y é obrigatório") Double eixoY,
        @NotNull(message = "O eixo Z é obrigatório") Double eixoZ
) {}
