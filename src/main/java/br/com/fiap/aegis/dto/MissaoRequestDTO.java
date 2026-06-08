package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusMissao;

public record MissaoRequestDTO(
        Long droneId,
        Long detritoId,
        StatusMissao statusMissao
) {}