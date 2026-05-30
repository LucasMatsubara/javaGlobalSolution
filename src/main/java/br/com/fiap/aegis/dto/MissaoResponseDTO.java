package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusMissao;

import java.time.LocalDateTime;

public record MissaoResponseDTO(
        Long droneId,
        Long detritoId,
        String nomeDrone,
        String nomeDetrito,
        LocalDateTime dataMissao,
        StatusMissao statusMissao
) {}
