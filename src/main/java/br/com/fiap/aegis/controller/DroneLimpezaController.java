package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.service.DroneLimpezaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/drones")
@Tag(name = "Drones de Limpeza", description = "Endpoints para fabricação e monitoramento de drones Chasers da frota")
public class DroneLimpezaController {

    @Autowired
    private DroneLimpezaService droneService;

    @PostMapping
    @Operation(summary = "Fabricar novo Drone")
    public ResponseEntity<EntityModel<DroneResponseDTO>> cadastrarDrone(@Valid @RequestBody DroneRequestDTO dto) {
        DroneResponseDTO response = droneService.cadastrarDrone(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarEntityModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar nome do Drone")
    public ResponseEntity<EntityModel<DroneResponseDTO>> atualizarDrone(@PathVariable Long id, @Valid @RequestBody DroneRequestDTO dto) {
        DroneResponseDTO response = droneService.atualizarDrone(id, dto);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar/Excluir Unidade da Frota")
    public ResponseEntity<Void> deletarDrone(@PathVariable Long id) {
        droneService.deletarDrone(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Drone por ID")
    public ResponseEntity<EntityModel<DroneResponseDTO>> buscarPorId(@PathVariable Long id) {
        DroneResponseDTO response = droneService.buscarPorId(id);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @GetMapping
    @Operation(summary = "Listar todos os Drones")
    public ResponseEntity<CollectionModel<EntityModel<DroneResponseDTO>>> listarTodos() {
        List<EntityModel<DroneResponseDTO>> drones = droneService.listarTodos().stream()
                .map(this::criarEntityModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<DroneResponseDTO>> collectionModel = CollectionModel.of(drones);
        collectionModel.add(linkTo(methodOn(DroneLimpezaController.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<DroneResponseDTO> criarEntityModel(DroneResponseDTO response) {
        EntityModel<DroneResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DroneLimpezaController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(DroneLimpezaController.class).atualizarDrone(response.id(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(DroneLimpezaController.class).deletarDrone(response.id())).withRel("deletar"));
        resource.add(linkTo(methodOn(DroneLimpezaController.class).listarTodos()).withRel("todos-drones"));
        return resource;
    }
}