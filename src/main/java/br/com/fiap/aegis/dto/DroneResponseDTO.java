package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusOperacional;

public record DroneResponseDTO(
        Long id,
        String nome,
        Double nivelBateria,
        StatusOperacional statusOperacional
) {}
