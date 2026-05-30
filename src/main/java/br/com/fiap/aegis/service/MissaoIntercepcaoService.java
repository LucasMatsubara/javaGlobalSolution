package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.MissaoRequestDTO;
import br.com.fiap.aegis.dto.MissaoResponseDTO;
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
        // 1. Valida se o Drone e o Detrito existem
        DroneLimpeza drone = droneRepository.findById(dto.droneId())
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + dto.droneId()));

        DetritoEspacial detrito = detritoRepository.findById(dto.detritoId())
                .orElseThrow(() -> new ResourceNotFoundException("Detrito não encontrado com ID: " + dto.detritoId()));

        // TODO: Aqui no futuro você pode colocar uma regra do tipo:
        // Se drone.getStatusOperacional() != "EM_BASE", lançar exceção dizendo que ele não pode ser despachado.

        // monta chave composta
        MissaoId missaoId = new MissaoId(drone.getId(), detrito.getId());

        // monta entidade associativa
        MissaoIntercepcao missao = new MissaoIntercepcao();
        missao.setId(missaoId);
        missao.setDrone(drone);
        missao.setDetrito(detrito);
        missao.setStatusMissao(dto.statusMissao());
        missao.setDataMissao(LocalDateTime.now()); // Registra o momento exato do despacho

        // atualiza o status do Drone para refletir a missão
        drone.setStatusOperacional("EM_MISSAO");
        droneRepository.save(drone);

        // salva a missão
        MissaoIntercepcao missaoSalva = missaoRepository.save(missao);

        // retorna o DTO
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