package br.com.fiap.aegis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MissaoRequestDTO(
        @NotNull(message = "O ID do Drone é obrigatório") Long droneId,
        @NotNull(message = "O ID do Detrito é obrigatório") Long detritoId,
        @NotBlank(message = "O status da missão é obrigatório") String statusMissao
) {}