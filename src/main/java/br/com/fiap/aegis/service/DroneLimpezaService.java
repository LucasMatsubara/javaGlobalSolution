package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
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

    public DroneResponseDTO cadastrarDrone(DroneRequestDTO dto) {
        DroneLimpeza drone = new DroneLimpeza();
        drone.setNome(dto.nome());
        DroneLimpeza droneSalvo = droneRepository.save(drone);
        logService.registarAcao(droneSalvo.getNome(), "Nova unidade de interceptação fabricada.", "SISTEMA");
        return mapearParaResponseDTO(droneSalvo);
    }

    public DroneResponseDTO atualizarDrone(Long id, DroneRequestDTO dto) {
        DroneLimpeza drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + id));
        drone.setNome(dto.nome());
        DroneLimpeza droneAtualizado = droneRepository.save(drone);
        logService.registarAcao(droneAtualizado.getNome(), "Designação da unidade alterada.", "SISTEMA");
        return mapearParaResponseDTO(droneAtualizado);
    }

    public void deletarDrone(Long id) {
        DroneLimpeza drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + id));
        droneRepository.delete(drone);
        logService.registarAcao(drone.getNome(), "Unidade de interceptação desativada e sucateada.", "ALTO");
    }

    public Page<DroneResponseDTO> listarTodosPaginado(Pageable pageable) {
        return droneRepository.findAll(pageable).map(this::mapearParaResponseDTO);
    }

    public DroneResponseDTO buscarPorId(Long id) {
        DroneLimpeza drone = droneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com ID: " + id));
        return mapearParaResponseDTO(drone);
    }

    private DroneResponseDTO mapearParaResponseDTO(DroneLimpeza drone) {
        return new DroneResponseDTO(drone.getId(), drone.getNome(), drone.getNivelBateria(), drone.getStatusOperacional());
    }
}