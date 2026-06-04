package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.RiscoColisao;

public record DetritoResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        Double velocidade,
        CoordenadaDTO coordenadas,
        String riscoColisao,
        String origen
) {}