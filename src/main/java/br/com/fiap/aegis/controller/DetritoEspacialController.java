package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DetritoRequestDTO;
import br.com.fiap.aegis.dto.DetritoResponseDTO;
import br.com.fiap.aegis.service.DetritoEspacialService;
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
@RequestMapping("/api/detritos")
@Tag(name = "Detritos Espaciais", description = "Endpoints para catalogação e radar de rastreamento de ameaças orbitais")
public class DetritoEspacialController {

    @Autowired
    private DetritoEspacialService detritoService;

    @PostMapping
    @Operation(summary = "Registrar novo detrito espacial")
    public ResponseEntity<EntityModel<DetritoResponseDTO>> registrarDetrito(@Valid @RequestBody DetritoRequestDTO dto) {
        DetritoResponseDTO response = detritoService.registrarDetrito(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarEntityModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados da Ameaça")
    public ResponseEntity<EntityModel<DetritoResponseDTO>> atualizarDetrito(@PathVariable Long id, @Valid @RequestBody DetritoRequestDTO dto) {
        DetritoResponseDTO response = detritoService.atualizarDetrito(id, dto);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover/Descartar Ameaça do Radar")
    public ResponseEntity<Void> deletarDetrito(@PathVariable Long id) {
        detritoService.deletarDetrito(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detrito por ID")
    public ResponseEntity<EntityModel<DetritoResponseDTO>> buscarPorId(@PathVariable Long id) {
        DetritoResponseDTO response = detritoService.buscarPorId(id);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @GetMapping
    @Operation(summary = "Listar todos os detritos")
    public ResponseEntity<CollectionModel<EntityModel<DetritoResponseDTO>>> listarTodos() {
        List<EntityModel<DetritoResponseDTO>> detritos = detritoService.listarTodos().stream()
                .map(this::criarEntityModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<DetritoResponseDTO>> collectionModel = CollectionModel.of(detritos);
        collectionModel.add(linkTo(methodOn(DetritoEspacialController.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<DetritoResponseDTO> criarEntityModel(DetritoResponseDTO response) {
        EntityModel<DetritoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DetritoEspacialController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(DetritoEspacialController.class).atualizarDetrito(response.id(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(DetritoEspacialController.class).deletarDetrito(response.id())).withRel("deletar"));
        resource.add(linkTo(methodOn(DetritoEspacialController.class).listarTodos()).withRel("todos-detritos"));
        return resource;
    }
}