package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.RiscoColisao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

public record DetritoRequestDTO(
        @NotBlank String nome,
        @NotNull @Positive Double massaKg,
        @NotNull @Valid CoordenadaDTO coordenadas,
        @NotNull RiscoColisao riscoColisao,
        @NotBlank String origem
) {}
