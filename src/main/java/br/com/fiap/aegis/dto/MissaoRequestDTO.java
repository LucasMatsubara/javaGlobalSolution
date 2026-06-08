package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusMissao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record MissaoRequestDTO(

        @NotNull(message = "O ID do drone interceptador é obrigatório")
        @Positive(message = "O ID do drone deve ser um número válido e positivo")
        Long droneId,

        @NotNull(message = "O ID do detrito alvo é obrigatório")
        @Positive(message = "O ID do detrito deve ser um número válido e positivo")
        Long detritoId,

        @NotNull(message = "O status inicial da missão é obrigatório")
        StatusMissao statusMissao
) {}