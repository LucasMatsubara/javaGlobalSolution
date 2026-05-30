package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.TipoBanda;

public record SateliteResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        CoordenadaDTO coordenadas,
        TipoBanda tipoBanda,
        EmpresaResponseDTO empresa
) {}
