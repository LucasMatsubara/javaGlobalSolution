package br.com.fiap.aegis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
        @JsonProperty("token") String token
) {}