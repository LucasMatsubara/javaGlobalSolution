package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DroneRequestDTO;
import br.com.fiap.aegis.dto.DroneResponseDTO;
import br.com.fiap.aegis.security.EmpresaResolver;
import br.com.fiap.aegis.service.DroneLimpezaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/drones")
@Tag(name = "Drones de Limpeza", description = "Endpoints paginados para controle e auditoria da frota planetária")
public class DroneLimpezaController {

    @Autowired
    private DroneLimpezaService droneService;

    @Autowired
    private EmpresaResolver empresaResolver;

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
    @Operation(summary = "Listar Drones — filtrado pela empresa do usuário logado")
    public ResponseEntity<PagedModel<EntityModel<DroneResponseDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<DroneResponseDTO> pagedResourcesAssembler) {

        Pageable pageable = PageRequest.of(page, size);
        Long empresaId = empresaResolver.getEmpresaIdDoUsuarioLogado();

        Page<DroneResponseDTO> dronesPaginados = (empresaId != null)
                ? droneService.listarPorEmpresa(empresaId, pageable)
                : droneService.listarTodosPaginado(pageable);

        PagedModel<EntityModel<DroneResponseDTO>> pagedModel = pagedResourcesAssembler.toModel(
                dronesPaginados, this::criarEntityModel);

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<DroneResponseDTO> criarEntityModel(DroneResponseDTO response) {
        EntityModel<DroneResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DroneLimpezaController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(DroneLimpezaController.class).atualizarDrone(response.id(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(DroneLimpezaController.class).deletarDrone(response.id())).withRel("deletar"));
        resource.add(linkTo(methodOn(DroneLimpezaController.class).listarTodos(0, 10, null)).withRel("todos-drones"));
        return resource;
    }
}