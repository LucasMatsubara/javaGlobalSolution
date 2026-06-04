package br.com.fiap.aegis.dto;

public record DashboardResponseDTO(
        Long satelitesAtivos,
        Long dronesEmMissao,
        Long ameacasCriticas,
        Double saudeOrbital,
        String statusSaude,
        String riscoColisaoGeral,
        Double cobertura,
        Long logsDeSistema,
        Long alertasHoje
) {}