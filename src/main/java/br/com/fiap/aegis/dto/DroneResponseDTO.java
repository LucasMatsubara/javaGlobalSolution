package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.enums.TipoBanda;

public record DroneResponseDTO(
        Long id,
        String nome,
        Double nivelBateria,
        StatusOperacional statusOperacional,
        TipoBanda tipoBanda
) {}