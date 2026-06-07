package br.com.fiap.aegis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterResponseDTO(
        @JsonProperty("mensagem") String mensagem,
        @JsonProperty("fazerLogin") String fazerLogin
) {}