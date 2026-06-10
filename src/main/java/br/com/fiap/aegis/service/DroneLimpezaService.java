package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.enums.TipoBanda;
import br.com.fiap.aegis.model.DroneLimpeza;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DroneLimpezaService {

    @Autowired
    private DroneLimpezaRepository droneRepository;

    @Autowired
    private LogOperacaoService logService;

    @Autowired
    private br.com.fiap.aegis.repository.EmpresaRepository empresaRepository;

    public DroneResponseDTO cadastrarDrone(DroneRequestDTO dto) {
        DroneLimpeza drone = new DroneLimpeza();
        drone.setNome(dto.nome());
        drone.setTipoBanda(dto.tipoBanda() != null ? dto.tipoBanda() : TipoBanda.BANDA_KA);
        if (dto.empresaId() != null) {
            empresaRepository.findById(dto.empresaId()).ifPresent(drone::setEmpresa);
        }
        DroneLimpeza droneSalvo = droneRepository.save(drone);
        logService.registarAcao(droneSalvo.getNome(),
                "Nova unidade de interceptação fabricada.", "SISTEMA", droneSalvo.getEmpresa());
        return mapearParaResponseDTO(droneSalvo);
    }

    public DroneResponseDTO atualizarDrone(Long id, DroneRequestDTO dto) {
        DroneLimpeza drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + id));
        drone.setNome(dto.nome());
        if (dto.tipoBanda() != null) drone.setTipoBanda(dto.tipoBanda());
        DroneLimpeza droneAtualizado = droneRepository.save(drone);
        logService.registarAcao(droneAtualizado.getNome(),
                "Designação da unidade alterada.", "SISTEMA", droneAtualizado.getEmpresa());
        return mapearParaResponseDTO(droneAtualizado);
    }

    public void deletarDrone(Long id) {
        DroneLimpeza drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + id));
        droneRepository.delete(drone);
        logService.registarAcao(drone.getNome(),
                "Unidade de interceptação desativada e sucateada.", "ALTO", drone.getEmpresa());
    }

    public Page<DroneResponseDTO> listarTodosPaginado(Pageable pageable) {
        return droneRepository.findAll(pageable).map(this::mapearParaResponseDTO);
    }

    public Page<DroneResponseDTO> listarPorEmpresa(Long empresaId, Pageable pageable) {
        return droneRepository.findByEmpresaId(empresaId, pageable).map(this::mapearParaResponseDTO);
    }

    public DroneResponseDTO buscarPorId(Long id) {
        DroneLimpeza drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + id));
        return mapearParaResponseDTO(drone);
    }

    private DroneResponseDTO mapearParaResponseDTO(DroneLimpeza drone) {
        return new DroneResponseDTO(
                drone.getId(),
                drone.getNome(),
                drone.getNivelBateria(),
                drone.getStatusOperacional(),
                drone.getTipoBanda()
        );
    }
}