package br.com.fiap.aegis.service;

import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.model.DroneLimpeza;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RotinaFrotaService {

    @Autowired
    private DroneLimpezaRepository droneRepository;

    @Autowired
    private LogOperacaoService logService;

    @Scheduled(fixedRate = 20000)
    public void processarRecargaERetornoDrones() {
        List<DroneLimpeza> dronesInterceptando = droneRepository.findAll()
                .stream()
                .filter(d -> d.getStatusOperacional() == StatusOperacional.INTERCEPTANDO)
                .toList();

        for (DroneLimpeza drone : dronesInterceptando) {
            drone.setStatusOperacional(StatusOperacional.RETORNANDO);
            droneRepository.save(drone);
            logService.registarAcao(drone.getNome(), "Missão cumprida. Retornando para a base orbital para recarga.", "INFO");
        }

        List<DroneLimpeza> dronesParaCarregar = droneRepository.findAll()
                .stream()
                .filter(d -> d.getStatusOperacional() == StatusOperacional.RETORNANDO || d.getNivelBateria() < 100)
                .toList();

        for (DroneLimpeza drone : dronesParaCarregar) {
            double novaBateria = drone.getNivelBateria() + 25.0;

            if (novaBateria >= 100.0) {
                drone.setNivelBateria(100.0);
                if (drone.getStatusOperacional() == StatusOperacional.RETORNANDO) {
                    drone.setStatusOperacional(StatusOperacional.NA_BASE);
                    logService.registarAcao(drone.getNome(), "Unidade acoplada à base. Bateria 100% Nominal.", "SISTEMA");
                }
            } else {
                drone.setNivelBateria(novaBateria);
            }
            droneRepository.save(drone);
        }
    }
}
