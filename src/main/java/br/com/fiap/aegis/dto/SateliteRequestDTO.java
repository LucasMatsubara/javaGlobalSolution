package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusSatelite;
import br.com.fiap.aegis.enums.TipoBanda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.Valid;

import java.time.LocalDate;

public record SateliteRequestDTO(
        @NotBlank(message = "O nome do satélite é obrigatório")
        String nome,

        @NotNull(message = "O NORAD ID é obrigatório")
        @Positive(message = "O NORAD ID deve ser um número de catálogo válido e positivo")
        Long noradId,

        @NotNull(message = "A inclinação orbital é obrigatória")
        Double inclinacao,

        @NotNull(message = "A data de lançamento é obrigatória")
        LocalDate dataLancamento,

        @NotNull(message = "O status operacional do satélite é obrigatório (ex: ATIVO, INATIVO)")
        StatusSatelite statusSatelite,

        @NotNull(message = "A massa do satélite é obrigatória")
        @Positive(message = "A massa do satélite deve ser um valor maior que zero (kg)")
        Double massaKg,

        @NotNull(message = "As coordenadas orbitais são obrigatórias")
        @Valid
        CoordenadaDTO coordenadas,

        @NotNull(message = "O tipo de banda de comunicação é obrigatório (ex: BANDA_KU, BANDA_KA)")
        TipoBanda tipoBanda,

        @NotNull(message = "O ID da empresa aeroespacial proprietária é obrigatório")
        @Positive(message = "O ID da empresa deve ser um número válido e positivo")
        Long empresaId
) {}