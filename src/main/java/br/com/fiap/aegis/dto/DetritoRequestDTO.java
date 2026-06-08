package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.enums.TipoDetrito;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record DetritoRequestDTO(

        @NotBlank(message = "O nome do detrito não pode estar em branco")
        String nome,

        @NotNull(message = "A massa (kg) é obrigatória")
        @PositiveOrZero(message = "A massa não pode ser um valor negativo")
        Double massaKg,

        @NotNull(message = "A velocidade é obrigatória")
        @PositiveOrZero(message = "A velocidade não pode ser negativa")
        Double velocidade,

        @NotNull(message = "O risco de colisão é obrigatório (ex: BAIXO, ALTO)")
        RiscoColisao riscoColisao,

        @NotNull(message = "O tipo do detrito é obrigatório")
        TipoDetrito tipoDetrito,

        @NotBlank(message = "A origem do detrito é obrigatória")
        String origem,

        @NotNull(message = "As coordenadas orbitais são obrigatórias")
        @Valid // Importante: Garante que o Spring também valide as regras lá dentro do CoordenadaDTO
        CoordenadaDTO coordenadas
) {}