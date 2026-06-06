package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.StatusSatelite;
import br.com.fiap.aegis.enums.TipoBanda;

import java.time.LocalDate;

public record SateliteResponseDTO(
        Long id,
        String nome,
        Long noradId,
        Double inclinacao,
        LocalDate dataLancamento,
        StatusSatelite statusSatelite,
        Double massaKg,
        TipoBanda tipoBanda,
        CoordenadaDTO coordenadas,
        EmpresaResponseDTO empresa
) {}