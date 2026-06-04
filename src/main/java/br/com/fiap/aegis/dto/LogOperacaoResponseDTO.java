package br.com.fiap.aegis.dto;

import java.time.LocalDateTime;

public record LogOperacaoResponseDTO(
        Long id,
        String entidadeAlvo,
        String descricao,
        String nivelGravidade,
        LocalDateTime dataHora
) {}
