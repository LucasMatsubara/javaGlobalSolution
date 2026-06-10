package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.MissaoRequestDTO;
import br.com.fiap.aegis.dto.MissaoResponseDTO;
import br.com.fiap.aegis.enums.*;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.*;
import br.com.fiap.aegis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissaoIntercepcaoService {

    @Autowired
    private MissaoIntercepcaoRepository missaoRepository;

    @Autowired
    private DroneLimpezaRepository droneRepository;

    @Autowired
    private DetritoEspacialRepository detritoRepository;

    @Autowired
    private LogOperacaoService logService;

    public MissaoResponseDTO despacharDrone(MissaoRequestDTO dto) {
        DroneLimpeza drone = droneRepository.findById(dto.droneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + dto.droneId()));

        DetritoEspacial detrito = detritoRepository.findById(dto.detritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + dto.detritoId()));

        if (drone.getStatusOperacional() != StatusOperacional.NA_BASE) {
            throw new IllegalStateException("OPERAÇÃO NEGADA: Não há drones disponíveis na base! Fabrique mais ou aguarde o retorno.");
        }

        boolean droneJaVoando = missaoRepository.findByDroneId(drone.getId()).stream()
                .anyMatch(m -> m.getStatusMissao() == StatusMissao.EM_ANDAMENTO);
        if (droneJaVoando) {
            throw new IllegalStateException("OPERAÇÃO NEGADA: Este drone já está em voo numa missão ativa.");
        }

        List<MissaoIntercepcao> missoesPendentes = missaoRepository.findByDroneId(drone.getId()).stream()
                .filter(m -> m.getStatusMissao() == StatusMissao.AUTORIZADA)
                .collect(Collectors.toList());
        for (MissaoIntercepcao pendente : missoesPendentes) {
            pendente.setStatusMissao(StatusMissao.ABORTADA);
            missaoRepository.save(pendente);
        }

        double consumo = calcularConsumoPorRisco(detrito.getRiscoColisao());

        if (!bandaIdealParaDetrito(drone.getTipoBanda(), detrito.getTipoDetrito())) {
            consumo += 12.0;
        }

        if (drone.getNivelBateria() < consumo) {
            throw new IllegalStateException("Drone com bateria insuficiente. Requer: " + consumo + "%, Atual: " + drone.getNivelBateria() + "%");
        }

        drone.setNivelBateria(drone.getNivelBateria() - consumo);
        drone.setStatusOperacional(StatusOperacional.INTERCEPTANDO);
        droneRepository.save(drone);

        MissaoId missaoId = new MissaoId(drone.getId(), detrito.getId());
        MissaoIntercepcao missao = new MissaoIntercepcao();
        missao.setId(missaoId);
        missao.setDrone(drone);
        missao.setDetrito(detrito);
        missao.setStatusMissao(StatusMissao.EM_ANDAMENTO);
        missao.setDataMissao(LocalDateTime.now());

        MissaoIntercepcao missaoSalva = missaoRepository.save(missao);

        logService.registarAcao(drone.getNome(),
                "Drone despachado para interceptar " + detrito.getNome() + ".",
                detrito.getRiscoColisao().name(), drone.getEmpresa());

        return mapearParaResponseDTO(missaoSalva);
    }

    private double calcularConsumoPorRisco(RiscoColisao risco) {
        return switch (risco) {
            case BAIXO -> 10.0;
            case MODERADO -> 25.0;
            case ALTO -> 40.0;
            case CRITICO -> 75.0;
        };
    }

    private boolean bandaIdealParaDetrito(TipoBanda banda, TipoDetrito tipo) {
        if (tipo == null || banda == null) return true;
        return switch (tipo) {
            case FRAGMENTO_FOGUETE -> banda == TipoBanda.BANDA_KA;
            case SATELITE_INATIVO  -> banda == TipoBanda.BANDA_KU;
            case PAINEL_SOLAR      -> banda == TipoBanda.BANDA_C;
            case DEBRIS_METALICO   -> banda == TipoBanda.BANDA_S;
            case MICRODEBRIS       -> banda == TipoBanda.BANDA_L;
        };
    }

    public MissaoResponseDTO lancarMissao(Long droneId, Long detritoId) {
        MissaoId id = new MissaoId(droneId, detritoId);
        MissaoIntercepcao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada."));

        DroneLimpeza drone = missao.getDrone();

        if (drone.getStatusOperacional() != StatusOperacional.NA_BASE) {
            throw new IllegalStateException("OPERAÇÃO NEGADA: Não há drones disponíveis na base! Fabrique mais ou aguarde o retorno.");
        }

        double consumo = calcularConsumoPorRisco(missao.getDetrito().getRiscoColisao());

        if (!bandaIdealParaDetrito(drone.getTipoBanda(), missao.getDetrito().getTipoDetrito())) {
            consumo += 12.0;
        }

        if (drone.getNivelBateria() < consumo) {
            throw new IllegalStateException("Drone com bateria insuficiente. Requer: " + consumo + "%, Atual: " + drone.getNivelBateria() + "%");
        }

        drone.setNivelBateria(drone.getNivelBateria() - consumo);
        drone.setStatusOperacional(StatusOperacional.INTERCEPTANDO);
        droneRepository.save(drone);

        missao.setStatusMissao(StatusMissao.EM_ANDAMENTO);
        missaoRepository.save(missao);

        logService.registarAcao(drone.getNome(),
                "Drone despachado para interceptar " + missao.getDetrito().getNome() + ".",
                missao.getDetrito().getRiscoColisao().name(), drone.getEmpresa());

        return mapearParaResponseDTO(missao);
    }

    public List<MissaoResponseDTO> listarTodas() {
        return missaoRepository.findAll().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
    }

    public MissaoResponseDTO buscarPorIdComposto(Long droneId, Long detritoId) {
        MissaoId id = new MissaoId(droneId, detritoId);
        MissaoIntercepcao missao = missaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Missão não encontrada."));
        return mapearParaResponseDTO(missao);
    }

    private MissaoResponseDTO mapearParaResponseDTO(MissaoIntercepcao missao) {
        return new MissaoResponseDTO(
                missao.getDrone().getId(),
                missao.getDetrito().getId(),
                missao.getDrone().getNome(),
                missao.getDetrito().getNome(),
                missao.getDataMissao(),
                missao.getStatusMissao(),
                missao.getDrone().getTipoBanda(),
                missao.getDetrito().getTipoDetrito()
        );
    }
}