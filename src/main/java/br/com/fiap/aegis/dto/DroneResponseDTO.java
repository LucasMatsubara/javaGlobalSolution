package br.com.fiap.aegis.dto;

public record DroneResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        CoordenadaDTO coordenadas,
        Double nivelBateria,
        String statusOperacional
) {}
