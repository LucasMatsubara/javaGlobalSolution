package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.enums.TipoDetrito;

public record DetritoResponseDTO(
        Long id,
        String nome,
        Double massaKg,
        Double velocidade,
        CoordenadaDTO coordenadas,
        RiscoColisao riscoColisao,
        TipoDetrito tipoDetrito,
        String origem
) {}