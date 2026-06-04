package br.com.fiap.aegis.dto;

// devolve o ID para o mobile
public record EmpresaResponseDTO(
        Long id,
        String nome,
        String cnpj
) {}
