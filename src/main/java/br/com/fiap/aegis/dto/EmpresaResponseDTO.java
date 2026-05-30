package br.com.fiap.aegis.dto;

// devolve o ID para o front-end
public record EmpresaResponseDTO(
        Long id,
        String nome,
        String cnpj
) {}
