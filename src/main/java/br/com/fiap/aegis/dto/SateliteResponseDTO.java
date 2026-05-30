package br.com.fiap.aegis.dto;

public record SateliteResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        CoordenadaDTO coordenadas,
        String tipoBanda,
        EmpresaResponseDTO empresa
) {}
