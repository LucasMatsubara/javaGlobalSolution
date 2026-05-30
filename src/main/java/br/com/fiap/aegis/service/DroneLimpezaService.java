package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.CoordenadaDTO;
import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.CoordenadaOrbital;
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
        drone.setNome(dto.nome());
        drone.setMassaKg(dto.massaKg());
        drone.setNivelBateria(dto.nivelBateria());
        drone.setStatusOperacional(dto.statusOperacional());

        CoordenadaOrbital coordenadas = new CoordenadaOrbital();
        coordenadas.setEixoX(dto.coordenadas().eixoX());
        coordenadas.setEixoY(dto.coordenadas().eixoY());
        coordenadas.setEixoZ(dto.coordenadas().eixoZ());
        drone.setCoordenadas(coordenadas);

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
                .orElseThrow(() -> new ResourceNotFoundException("Drone não encontrado com o ID: " + id));
        return mapearParaResponseDTO(drone);
    }

    private DroneResponseDTO mapearParaResponseDTO(DroneLimpeza drone) {
        CoordenadaDTO coordDTO = new CoordenadaDTO(
                drone.getCoordenadas().getEixoX(),
                drone.getCoordenadas().getEixoY(),
                drone.getCoordenadas().getEixoZ()
        );

        return new DroneResponseDTO(
                drone.getId(),
                drone.getNome(),
                drone.getMassaKg(),
                coordDTO,
                drone.getNivelBateria(),
                drone.getStatusOperacional()
        );
    }
}
