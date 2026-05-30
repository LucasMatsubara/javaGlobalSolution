package br.com.fiap.aegis.dto;

import java.time.LocalDateTime;

public record MissaoResponseDTO(
        Long droneId,
        Long detritoId,
        String nomeDrone,
        String nomeDetrito,
        LocalDateTime dataMissao,
        String statusMissao
) {}
