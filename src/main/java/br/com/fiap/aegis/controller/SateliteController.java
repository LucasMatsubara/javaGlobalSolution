package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.SateliteRequestDTO;
import br.com.fiap.aegis.dto.SateliteResponseDTO;
import br.com.fiap.aegis.service.SateliteService;
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
@RequestMapping("/api/satelites")
@Tag(name = "Satélites Comerciais", description = "Endpoints para lançamento, monitoramento e gestão da base de satélites ativos")
public class SateliteController {

    @Autowired
    private SateliteService sateliteService;

    @PostMapping
    @Operation(summary = "Lançar novo Satélite", description = "Registra um novo satélite com telemetria inicial")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> cadastrarSatelite(@Valid @RequestBody SateliteRequestDTO dto) {
        SateliteResponseDTO response = sateliteService.cadastrarSatelite(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarEntityModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Satélite", description = "Altera os parâmetros de órbita ou status do satélite (Botão Editar do App)")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> atualizarSatelite(@PathVariable Long id, @Valid @RequestBody SateliteRequestDTO dto) {
        SateliteResponseDTO response = sateliteService.atualizarSatelite(id, dto);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar/Remover Satélite", description = "Desativa e remove o satélite do radar (Botão Lixeira do App)")
    public ResponseEntity<Void> deletarSatelite(@PathVariable Long id) {
        sateliteService.deletarSatelite(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Satélite por ID")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> buscarPorId(@PathVariable Long id) {
        SateliteResponseDTO response = sateliteService.buscarPorId(id);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @GetMapping
    @Operation(summary = "Listar todos os Satélites")
    public ResponseEntity<CollectionModel<EntityModel<SateliteResponseDTO>>> listarTodos() {
        List<EntityModel<SateliteResponseDTO>> satelites = sateliteService.listarTodos().stream()
                .map(this::criarEntityModel)
                .collect(Collectors.toList());
        CollectionModel<EntityModel<SateliteResponseDTO>> collectionModel = CollectionModel.of(satelites);
        collectionModel.add(linkTo(methodOn(SateliteController.class).listarTodos()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    private EntityModel<SateliteResponseDTO> criarEntityModel(SateliteResponseDTO response) {
        EntityModel<SateliteResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(SateliteController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(SateliteController.class).atualizarSatelite(response.id(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(SateliteController.class).deletarSatelite(response.id())).withRel("deletar"));
        resource.add(linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos-satelites"));
        return resource;
    }
}