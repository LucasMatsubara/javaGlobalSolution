package br.com.fiap.aegis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(
        @JsonProperty("token") String token,
        @JsonProperty("empresaId") Long empresaId,
        @JsonProperty("nomeEmpresa") String nomeEmpresa
) {}