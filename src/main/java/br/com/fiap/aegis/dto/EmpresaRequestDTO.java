package br.com.fiap.aegis.dto;

import jakarta.validation.constraints.NotBlank;

public record EmpresaRequestDTO(
        @NotBlank(message = "O nome da empresa é obrigatório")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório")
        String cnpj
) {}
