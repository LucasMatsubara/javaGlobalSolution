package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.DashboardResponseDTO;
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
        // 1. Contagens Diretas
        long satelitesAtivos = sateliteRepository.countByStatusSatelite(StatusSatelite.ATIVO);
        long dronesEmMissao = droneRepository.countByStatusOperacional(StatusOperacional.INTERCEPTANDO);
        long ameacasCriticas = detritoRepository.countByRiscoColisaoIgnoreCase("CRITICO");
        long ameacasAltas = detritoRepository.countByRiscoColisaoIgnoreCase("ALTO");
        long logsTotais = logRepository.count();

        // Contar alertas que ocorreram apenas no dia atual
        LocalDateTime inicioDeHoje = LocalDate.now().atStartOfDay();
        long alertasHoje = logRepository.countByDataHoraAfter(inicioDeHoje);

        // 2. Matemática da Saúde Orbital
        double saudeOrbital = 100.0;
        // Cada ameaça crítica tira 15% de saúde, cada ameaça alta tira 5%
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

        // 4. Cobertura de Satélite (Exemplo: base de 80% + 2% por satélite ativo, teto de 100%)
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
