package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.enums.TipoDetrito;

public record DetritoRequestDTO(
        String nome,
        Double massaKg,
        Double velocidade,
        RiscoColisao riscoColisao,
        TipoDetrito tipoDetrito,
        String origem,
        CoordenadaDTO coordenadas
) {}