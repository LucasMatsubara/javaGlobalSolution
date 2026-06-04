package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.DroneLimpeza;
import br.com.fiap.aegis.repository.DroneLimpezaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DroneLimpezaService {

    @Autowired
    private DroneLimpezaRepository droneRepository;

    public DroneResponseDTO cadastrarDrone(DroneRequestDTO dto) {
        DroneLimpeza drone = new DroneLimpeza();
        drone.setNome(dto.nome()); // inicia automaticamente com 100% de bateria e NA_BASE

        DroneLimpeza droneSalvo = droneRepository.save(drone);
        return mapearParaResponseDTO(droneSalvo);
    }

    public List<DroneResponseDTO> listarTodos() {
        return droneRepository.findAll().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
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
                drone.getStatusOperacional()
        );
    }
}