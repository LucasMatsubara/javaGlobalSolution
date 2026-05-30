package br.com.fiap.aegis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

public record SateliteRequestDTO(
        @NotBlank(message = "O nome do satélite é obrigatório") String nome,
        @NotNull(message = "A massa é obrigatória") @Positive Double massaKg,
        @NotNull(message = "As coordenadas são obrigatórias") @Valid CoordenadaDTO coordenadas,
        @NotBlank(message = "O tipo de banda é obrigatório") String tipoBanda,
        @NotNull(message = "O ID da empresa proprietária é obrigatório") Long empresaId
) {}