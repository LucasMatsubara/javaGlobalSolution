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
    @Operation(summary = "Lançar novo Satélite", description = "Registra um novo satélite monitorado com dados do NORAD ID, altitude e inclinação")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> cadastrarSatelite(@Valid @RequestBody SateliteRequestDTO dto) {
        SateliteResponseDTO response = sateliteService.cadastrarSatelite(dto);

        EntityModel<SateliteResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(SateliteController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos-satelites"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Satélite por ID", description = "Retorna os detalhes técnicos, altitude e dados operacionais de um satélite específico")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> buscarPorId(@PathVariable Long id) {
        SateliteResponseDTO response = sateliteService.buscarPorId(id);

        EntityModel<SateliteResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(SateliteController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos-satelites"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os Satélites", description = "Retorna a frota global de satélites cadastrados no sistema da operadora")
    public ResponseEntity<CollectionModel<EntityModel<SateliteResponseDTO>>> listarTodos() {
        List<EntityModel<SateliteResponseDTO>> satelites = sateliteService.listarTodos().stream()
                .map(satelite -> EntityModel.of(satelite,
                        linkTo(methodOn(SateliteController.class).buscarPorId(satelite.id())).withSelfRel(),
                        linkTo(methodOn(SateliteController.class).listarTodos()).withRel("todos-satelites")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<SateliteResponseDTO>> collectionModel = CollectionModel.of(satelites);
        collectionModel.add(linkTo(methodOn(SateliteController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}