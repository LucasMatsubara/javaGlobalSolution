package br.com.fiap.aegis.service;

import br.com.fiap.aegis.enums.*;
import br.com.fiap.aegis.model.*;
import br.com.fiap.aegis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DroneSimuladorScheduler {

    @Autowired private DroneLimpezaRepository droneRepository;
    @Autowired private MissaoIntercepcaoRepository missaoRepository;
    @Autowired private DetritoEspacialRepository detritoRepository;
    @Autowired private LogOperacaoService logService;

    @Scheduled(fixedDelay = 20000)
    @Transactional
    public void simularCicloDrones() {
        try {
            simularDronesInterceptando();
            simularDronesRecolhendoLixo();
            simularDronesRetornando();
            recarregarDronesNaBase();
        } catch (Exception e) {
            System.err.println("[DroneSimulador] ERRO no ciclo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void simularDronesInterceptando() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.INTERCEPTANDO);
        for (DroneLimpeza drone : drones) {
            drone.setStatusOperacional(StatusOperacional.RECOLHENDO_LIXO);
            droneRepository.save(drone);
            logService.registarAcao(
                    drone.getNome(),
                    "Drone chegou ao alvo. Iniciando recolhimento do detrito.",
                    "SISTEMA",
                    drone.getEmpresa()
            );
        }
    }

    private void simularDronesRecolhendoLixo() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.RECOLHENDO_LIXO);
        for (DroneLimpeza drone : drones) {
            drone.setStatusOperacional(StatusOperacional.RETORNANDO);
            droneRepository.save(drone);

            // Busca apenas as missões EM_ANDAMENTO deste drone
            List<MissaoIntercepcao> missoes = missaoRepository.findByDroneId(drone.getId());
            for (MissaoIntercepcao missao : missoes) {
                if (missao.getStatusMissao() == StatusMissao.EM_ANDAMENTO) {
                    DetritoEspacial detrito = missao.getDetrito();
                    String nomeDetrito = detrito.getNome();
                    Empresa empresa = drone.getEmpresa();

                    // 1. Remove a missão primeiro — elimina a FK que aponta pro detrito
                    missaoRepository.delete(missao);
                    missaoRepository.flush(); // força o DELETE no banco antes do próximo passo

                    // 2. Agora o Oracle aceita deletar o detrito (nenhuma FK aponta mais para ele)
                    detritoRepository.delete(detrito);
                    detritoRepository.flush();

                    logService.registarAcao(
                            nomeDetrito,
                            "Ameaça neutralizada com sucesso por " + drone.getNome() + ".",
                            "INFO",
                            empresa
                    );
                }
            }

            logService.registarAcao(
                    drone.getNome(),
                    "Detrito coletado. Drone retornando para a base.",
                    "SISTEMA",
                    drone.getEmpresa()
            );
        }
    }

    private void simularDronesRetornando() {
        List<DroneLimpeza> drones = droneRepository.findByStatusOperacional(StatusOperacional.RETORNANDO);
        for (DroneLimpeza drone : drones) {
            drone.setStatusOperacional(StatusOperacional.NA_BASE);
            droneRepository.save(drone);
            logService.registarAcao(
                    drone.getNome(),
                    "Drone retornou à base com sucesso.",
                    "SISTEMA",
                    drone.getEmpresa()
            );
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