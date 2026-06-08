package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.DashboardResponseDTO;
import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.enums.StatusSatelite;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import br.com.fiap.aegis.repository.LogOperacaoRepository;
import br.com.fiap.aegis.repository.SateliteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class DashboardService {

    @Autowired
    private SateliteRepository sateliteRepository;

    @Autowired
    private DroneLimpezaRepository droneRepository;

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    @Autowired
    private LogOperacaoRepository logRepository;

    public DashboardResponseDTO obterResumoDashboard() {
        long satelitesAtivos = sateliteRepository.countByStatusSatelite(StatusSatelite.ATIVO);

        // Conta drones em qualquer fase de missão ativa
        long dronesEmMissao = droneRepository.countByStatusOperacional(StatusOperacional.INTERCEPTANDO)
                + droneRepository.countByStatusOperacional(StatusOperacional.RECOLHENDO_LIXO)
                + droneRepository.countByStatusOperacional(StatusOperacional.RETORNANDO);

        long ameacasCriticas = detritoRepository.countByRiscoColisao(RiscoColisao.CRITICO);
        long ameacasAltas = detritoRepository.countByRiscoColisao(RiscoColisao.ALTO);

        long logsTotais = logRepository.count();

        LocalDateTime inicioDeHoje = LocalDate.now().atStartOfDay();
        long alertasHoje = logRepository.countByDataHoraAfter(inicioDeHoje);

        double saudeOrbital = 100.0;
        saudeOrbital -= (ameacasCriticas * 15.0) + (ameacasAltas * 5.0);
        if (saudeOrbital < 0) saudeOrbital = 0.0;

        String statusSaude = "NOMINAL";
        String riscoGeral = "Baixo";

        if (saudeOrbital < 60.0) {
            statusSaude = "CRITICO";
            riscoGeral = "Alto";
        } else if (saudeOrbital < 90.0) {
            statusSaude = "DEGRADADO";
            riscoGeral = "Médio";
        }

        double cobertura = Math.min(100.0, 80.0 + (satelitesAtivos * 2.0));

        return new DashboardResponseDTO(
                satelitesAtivos,
                dronesEmMissao,
                ameacasCriticas,
                saudeOrbital,
                statusSaude,
                riscoGeral,
                cobertura,
                logsTotais,
                alertasHoje
        );
    }
}