package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.TipoBanda;

public record DroneRequestDTO(
        String nome,
        TipoBanda tipoBanda
) {}