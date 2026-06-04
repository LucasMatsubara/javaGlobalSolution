package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusSatelite;

import java.time.LocalDate;

public record SateliteResponseDTO(
        Long id,
        String nome,
        String noradId,
        Double inclinacao,
        LocalDate dataLancamento,
        StatusSatelite statusSatelite,
        Double massaKg,
        String tipoBanda,
        CoordenadaDTO coordenadas,
        EmpresaResponseDTO empresa
) {}