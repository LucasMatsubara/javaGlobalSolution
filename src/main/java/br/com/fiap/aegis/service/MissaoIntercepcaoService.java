package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.MissaoRequestDTO;
import br.com.fiap.aegis.dto.MissaoResponseDTO;
import br.com.fiap.aegis.enums.RiscoColisao;
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

    @Autowired
    private LogOperacaoService logService;

    public MissaoResponseDTO despacharDrone(MissaoRequestDTO dto) {
        DroneLimpeza drone = droneRepository.findById(dto.droneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + dto.droneId()));

        DetritoEspacial detrito = detritoRepository.findById(dto.detritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + dto.detritoId()));

        if (drone.getStatusOperacional() != StatusOperacional.NA_BASE) {
            throw new IllegalStateException("O Drone selecionado não está disponível. Status atual: " + drone.getStatusOperacional());
        }

        // Regra de Consumo Baseada no Enum RiscoColisao
        double consumo;
        RiscoColisao risco = detrito.getRiscoColisao();

        if (risco == RiscoColisao.MODERADO) {
            consumo = 20.0;
        } else if (risco == RiscoColisao.ALTO) {
            consumo = 50.0;
        } else if (risco == RiscoColisao.CRITICO) {
            consumo = 80.0;
        } else {
            consumo = 10.0; // BAIXO
        }

        if (drone.getNivelBateria() < consumo) {
            throw new IllegalStateException("Drone com bateria insuficiente para esta missão. Requer: " + consumo + "%, Atual: " + drone.getNivelBateria() + "%");
        }

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

        logService.registarAcao(
                drone.getNome(),
                "Drone despachado para interceptar " + detrito.getNome() + ".",
                risco.name()
        );

        logService.registarAcao(
                detrito.getNome(),
                "Ameaça neutralizada com sucesso por " + drone.getNome() + ".",
                "INFO"
        );

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