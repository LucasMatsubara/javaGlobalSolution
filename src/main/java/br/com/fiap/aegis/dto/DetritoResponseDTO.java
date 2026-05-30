package br.com.fiap.aegis.dto;

public record DetritoResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        CoordenadaDTO coordenadas,
        String riscoColisao,
        String origem
) {}
