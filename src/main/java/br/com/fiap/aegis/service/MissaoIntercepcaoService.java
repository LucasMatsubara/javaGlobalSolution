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

        // enum ó despacha se estiver na base
        if (drone.getStatusOperacional() != StatusOperacional.EM_BASE) {
            throw new IllegalStateException("O Drone não pode ser despachado. Status atual: " + drone.getStatusOperacional());
        }

        MissaoId missaoId = new MissaoId(drone.getId(), detrito.getId());

        MissaoIntercepcao missao = new MissaoIntercepcao();
        missao.setId(missaoId);
        missao.setDrone(drone);
        missao.setDetrito(detrito);
        missao.setStatusMissao(dto.statusMissao()); // Setando o Enum
        missao.setDataMissao(LocalDateTime.now());

        // Atualiza o enum do Drone para refletir a missão
        drone.setStatusOperacional(StatusOperacional.EM_MISSAO);
        droneRepository.save(drone);

        MissaoIntercepcao missaoSalva = missaoRepository.save(missao);

        return new MissaoResponseDTO(
                missaoSalva.getDrone().getId(),
                missaoSalva.getDetrito().getId(),
                missaoSalva.getDrone().getNome(),
                missaoSalva.getDetrito().getNome(),
                missaoSalva.getDataMissao(),
                missaoSalva.getStatusMissao()
        );
    }
}