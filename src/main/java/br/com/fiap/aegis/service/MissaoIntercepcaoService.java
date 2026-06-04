package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.MissaoRequestDTO;
import br.com.fiap.aegis.dto.MissaoResponseDTO;
import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.DetritoEspacial;
import br.com.fiap.aegis.model.DroneLimpeza;
import br.com.fiap.aegis.model.MissaoId;
import br.com.fiap.aegis.model.MissaoIntercepcao;
import br.com.fiap.aegis.repository.DetritoEspacialRepository;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import br.com.fiap.aegis.repository.MissaoIntercepcaoRepository;
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

    public MissaoResponseDTO despacharDrone(MissaoRequestDTO dto) {
        DroneLimpeza drone = droneRepository.findById(dto.droneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + dto.droneId()));

        DetritoEspacial detrito = detritoRepository.findById(dto.detritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + dto.detritoId()));

        // 1: verificar se o drone escolhido está livre na base
        if (drone.getStatusOperacional() != StatusOperacional.NA_BASE) {
            throw new IllegalStateException("O Drone selecionado não está disponível. Status atual: " + drone.getStatusOperacional());
        }

        // 2: calcular o consumo de bateria baseado no risco do detrito
        double consumo;
        String risco = detrito.getRiscoColisao().toUpperCase();
        if (risco.contains("MODERADO")) {
            consumo = 20.0;
        } else if (risco.contains("ALTO")) {
            consumo = 50.0;
        } else { // CRÍTICO
            consumo = 80.0;
        }

        // 3: verificar se o drone tem carga suficiente para a missão
        if (drone.getNivelBateria() < consumo) {
            throw new IllegalStateException("Drone com bateria insuficiente para esta missão. Requer: " + consumo + "%, Atual: " + drone.getNivelBateria() + "%");
        }

        // executar despacho e abater a bateria
        drone.setNivelBateria(drone.getNivelBateria() - consumo);
        drone.setStatusOperacional(StatusOperacional.INTERCEPTANDO);
        droneRepository.save(drone);

        MissaoId missaoId = new MissaoId(drone.getId(), detrito.getId());
        MissaoIntercepcao missao = new MissaoIntercepcao();
        missao.setId(missaoId);
        missao.setDrone(drone);
        missao.setDetrito(detrito);
        missao.setStatusMissao(dto.statusMissao());
        missao.setDataMissao(LocalDateTime.now());

        MissaoIntercepcao missaoSalva = missaoRepository.save(missao);
        return mapearParaResponseDTO(missaoSalva);
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
                missao.getStatusMissao()
        );
    }
}