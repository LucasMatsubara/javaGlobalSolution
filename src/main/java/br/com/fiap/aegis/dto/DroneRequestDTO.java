package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusOperacional;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

public record DroneRequestDTO(
        @NotBlank String nome,
        @NotNull @Positive Double massaKg,
        @NotNull @Valid CoordenadaDTO coordenadas,
        @NotNull @PositiveOrZero Double nivelBateria,
        @NotNull StatusOperacional statusOperacional
) {}
