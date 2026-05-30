package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.service.DroneLimpezaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/drones")
@Tag(name = "Drones de Limpeza (Chasers)", description = "Endpoints para gestão da frota de veículos autônomos interceptadores")
public class DroneLimpezaController {

    @Autowired
    private DroneLimpezaService droneService;

    @PostMapping
    @Operation(summary = "Cadastrar novo Drone", description = "Regista um novo drone Chaser na frota da plataforma")
    public ResponseEntity<EntityModel<DroneResponseDTO>> cadastrarDrone(@Valid @RequestBody DroneRequestDTO dto) {
        DroneResponseDTO response = droneService.cadastrarDrone(dto);
        EntityModel<DroneResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DroneLimpezaController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(DroneLimpezaController.class).listarTodos()).withRel("todos-drones"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Drone por ID", description = "Retorna os detalhes de um drone específico")
    public ResponseEntity<EntityModel<DroneResponseDTO>> buscarPorId(@PathVariable Long id) {
        DroneResponseDTO response = droneService.buscarPorId(id);

        EntityModel<DroneResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DroneLimpezaController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(DroneLimpezaController.class).listarTodos()).withRel("todos-drones"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os Drones", description = "Retorna o catálogo completo de drones cadastrados")
    public ResponseEntity<CollectionModel<EntityModel<DroneResponseDTO>>> listarTodos() {
        List<EntityModel<DroneResponseDTO>> drones = droneService.listarTodos().stream()
                .map(drone -> EntityModel.of(drone,
                        linkTo(methodOn(DroneLimpezaController.class).buscarPorId(drone.id())).withSelfRel(),
                        linkTo(methodOn(DroneLimpezaController.class).listarTodos()).withRel("todos-drones")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DroneResponseDTO>> collectionModel = CollectionModel.of(drones);
        collectionModel.add(linkTo(methodOn(DroneLimpezaController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}