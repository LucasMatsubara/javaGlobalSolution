package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusMissao;
import jakarta.validation.constraints.NotNull;

public record MissaoRequestDTO(
        @NotNull(message = "O ID do Drone é obrigatório") Long droneId,
        @NotNull(message = "O ID do Detrito é obrigatório") Long detritoId,
        @NotNull(message = "O status da missão é obrigatório") StatusMissao statusMissao
) {}