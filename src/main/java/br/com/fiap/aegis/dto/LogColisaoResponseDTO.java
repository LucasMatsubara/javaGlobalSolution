package br.com.fiap.aegis.dto;

import java.time.LocalDateTime;

public record LogColisaoResponseDTO(
        Long id,
        String nomeSatelite,
        String nomeDetrito,
        String descricaoAlerta,
        LocalDateTime dataRegistro
) {}
