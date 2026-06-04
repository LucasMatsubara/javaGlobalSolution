package br.com.fiap.aegis.dto;

public record DetritoResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        Double velocidade,
        CoordenadaDTO coordenadas,
        String riscoColisao,
        String origen
) {}