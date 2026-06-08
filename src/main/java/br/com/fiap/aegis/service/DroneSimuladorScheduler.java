package br.com.fiap.aegis.service;

import br.com.fiap.aegis.enums.StatusMissao;
import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.model.DroneLimpeza;
import br.com.fiap.aegis.model.MissaoIntercepcao;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import br.com.fiap.aegis.repository.MissaoIntercepcaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneSimuladorScheduler {

    @Autowired
    private DroneLimpezaRepository droneRepository;

    @Autowired
    private MissaoIntercepcaoRepository missaoRepository;

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    @Autowired
    private LogOperacaoService logService;

    @Scheduled(fixedDelay = 20000)
    public void simularCicloDrones() {
        simularDronesInterceptando();
        simularDronesRecolhendoLixo();
        simularDronesRetornando();
        recarregarDronesNaBase();
    }

    private void simularDronesInterceptando() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.INTERCEPTANDO);
        for (DroneLimpeza drone : drones) {
            drone.setStatusOperacional(StatusOperacional.RECOLHENDO_LIXO);
            droneRepository.save(drone);
            logService.registarAcao(drone.getNome(), "Drone chegou ao alvo. Iniciando recolhimento do detrito.", "SISTEMA");
        }
    }

    private void simularDronesRecolhendoLixo() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.RECOLHENDO_LIXO);
        for (DroneLimpeza drone : drones) {
            drone.setStatusOperacional(StatusOperacional.RETORNANDO);
            droneRepository.save(drone);

            List<MissaoIntercepcao> missoes = missaoRepository.findByDroneId(drone.getId());
            for (MissaoIntercepcao missao : missoes) {
                if (missao.getStatusMissao() == StatusMissao.EM_ANDAMENTO) {
                    missao.setStatusMissao(StatusMissao.CONCLUIDA_SUCESSO);
                    missaoRepository.save(missao);
                    logService.registarAcao(
                            missao.getDetrito().getNome(),
                            "Ameaça neutralizada com sucesso por " + drone.getNome() + ".",
                            "INFO"
                    );
                    detritoRepository.delete(missao.getDetrito());
                }
            }

            logService.registarAcao(drone.getNome(), "Detrito coletado. Drone retornando para a base.", "SISTEMA");
        }
    }

    private void simularDronesRetornando() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.RETORNANDO);
        for (DroneLimpeza drone : drones) {
            drone.setStatusOperacional(StatusOperacional.NA_BASE);
            droneRepository.save(drone);
            logService.registarAcao(drone.getNome(), "Drone retornou à base com sucesso.", "SISTEMA");
        }
    }

    private void recarregarDronesNaBase() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.NA_BASE);
        for (DroneLimpeza drone : drones) {
            if (drone.getNivelBateria() < 100.0) {
                drone.setNivelBateria(Math.min(100.0, drone.getNivelBateria() + 20.0));
                droneRepository.save(drone);
            }
        }
    }
}