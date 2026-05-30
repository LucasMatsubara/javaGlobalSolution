package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.service.DroneLimpezaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drones")
public class DroneLimpezaController {

    @Autowired
    private DroneLimpezaService droneService;

    @PostMapping
    public ResponseEntity<DroneResponseDTO> cadastrarDrone(@Valid @RequestBody DroneRequestDTO dto) {
        DroneResponseDTO response = droneService.cadastrarDrone(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DroneResponseDTO>> listarTodos() {
        List<DroneResponseDTO> response = droneService.listarTodos();
        return ResponseEntity.ok(response);
    }
}
