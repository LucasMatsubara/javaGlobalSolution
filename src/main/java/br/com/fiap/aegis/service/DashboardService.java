package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.DashboardResponseDTO;
import br.com.fiap.aegis.enums.*;
import br.com.fiap.aegis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.*;

@Service
public class DashboardService {

    @Autowired private SateliteRepository sateliteRepository;
    @Autowired private DroneLimpezaRepository droneRepository;
    @Autowired private DetritoEspacialRepository detritoRepository;
    @Autowired private LogOperacaoRepository logRepository;

    public DashboardResponseDTO obterResumoDashboard(Long empresaId) {

        long satelitesAtivos = sateliteRepository
                .countByEmpresaIdAndStatusSatelite(empresaId, StatusSatelite.ATIVO);

        long dronesEmMissao =
                droneRepository.countByEmpresaIdAndStatusOperacional(empresaId, StatusOperacional.INTERCEPTANDO)
                        + droneRepository.countByEmpresaIdAndStatusOperacional(empresaId, StatusOperacional.RECOLHENDO_LIXO)
                        + droneRepository.countByEmpresaIdAndStatusOperacional(empresaId, StatusOperacional.RETORNANDO);

        long ameacasCriticas = detritoRepository.countByEmpresaIdAndRiscoColisao(empresaId, RiscoColisao.CRITICO);
        long ameacasAltas    = detritoRepository.countByEmpresaIdAndRiscoColisao(empresaId, RiscoColisao.ALTO);

        long logsTotais  = logRepository.countByEmpresaId(empresaId);
        long alertasHoje = logRepository.countByEmpresaIdAndDataHoraAfter(
                empresaId, LocalDate.now().atStartOfDay());

        double saudeOrbital = Math.max(0.0, 100.0 - (ameacasCriticas * 15.0) - (ameacasAltas * 5.0));

        String statusSaude = saudeOrbital < 60.0 ? "CRITICO"  : saudeOrbital < 90.0 ? "DEGRADADO" : "NOMINAL";
        String riscoGeral  = saudeOrbital < 60.0 ? "Alto"     : saudeOrbital < 90.0 ? "Médio"     : "Baixo";
        double cobertura   = Math.min(100.0, 80.0 + (satelitesAtivos * 2.0));

        return new DashboardResponseDTO(satelitesAtivos, dronesEmMissao, ameacasCriticas,
                saudeOrbital, statusSaude, riscoGeral, cobertura, logsTotais, alertasHoje);
    }
}