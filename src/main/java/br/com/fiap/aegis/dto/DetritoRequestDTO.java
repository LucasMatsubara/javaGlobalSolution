package br.com.fiap.aegis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

public record DetritoRequestDTO(
        @NotBlank String nome,
        @NotNull @Positive Double massaKg,
        @NotNull @Valid CoordenadaDTO coordenadas,
        @NotBlank String riscoColisao,
        @NotBlank String origem
) {}
