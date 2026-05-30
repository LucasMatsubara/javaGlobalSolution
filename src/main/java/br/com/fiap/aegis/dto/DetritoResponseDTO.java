package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.RiscoColisao;

public record DetritoResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        CoordenadaDTO coordenadas,
        RiscoColisao riscoColisao,
        String origem
) {}
