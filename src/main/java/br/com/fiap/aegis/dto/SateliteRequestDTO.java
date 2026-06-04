package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusSatelite;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

import java.time.LocalDate;

public record SateliteRequestDTO(
        @NotBlank(message = "O nome do satélite é obrigatório") String nome,
        @NotBlank(message = "O NORAD ID é obrigatório") String noradId,
        @NotNull(message = "A inclinação é obrigatória") Double inclinacao,
        @NotNull(message = "A data de lançamento é obrigatória") LocalDate dataLancamento,
        @NotNull(message = "O status do satélite é obrigatório") StatusSatelite statusSatelite,
        @NotNull(message = "A massa é obrigatória") @Positive Double massaKg,
        @NotNull(message = "As coordenadas são obrigatórias") @Valid CoordenadaDTO coordenadas,
        @NotNull(message = "O tipo de banda é obrigatório") String tipoBanda,
        @NotNull(message = "O ID da empresa proprietária é obrigatório") Long empresaId
) {}