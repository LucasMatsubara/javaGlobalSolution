package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.SateliteRequestDTO;
import br.com.fiap.aegis.dto.SateliteResponseDTO;
import br.com.fiap.aegis.service.SateliteService;
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
@RequestMapping("/api/satelites")
@Tag(name = "Satélites Comerciais", description = "Endpoints paginados para monitoramento da frota de satélites ativos")
public class SateliteController {

    @Autowired
    private SateliteService sateliteService;

    @PostMapping
    @Operation(summary = "Lançar novo Satélite")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> cadastrarSatelite(@Valid @RequestBody SateliteRequestDTO dto) {
        SateliteResponseDTO response = sateliteService.cadastrarSatelite(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criarEntityModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Satélite")
    public ResponseEntity<EntityModel<SateliteResponseDTO>> atualizarSatelite(@PathVariable Long id, @Valid @RequestBody SateliteRequestDTO dto) {
        SateliteResponseDTO response = sateliteService.atualizarSatelite(id, dto);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar/Remover Satélite")
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
    @Operation(summary = "Listar todos os Satélites com Paginação")
    public ResponseEntity<PagedModel<EntityModel<SateliteResponseDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<SateliteResponseDTO> pagedResourcesAssembler) {

        Pageable pageable = PageRequest.of(page, size);
        Page<SateliteResponseDTO> satelitesPaginados = sateliteService.listarTodosPaginado(pageable);

        PagedModel<EntityModel<SateliteResponseDTO>> pagedModel = pagedResourcesAssembler.toModel(satelitesPaginados,
                this::criarEntityModel
        );

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<SateliteResponseDTO> criarEntityModel(SateliteResponseDTO response) {
        EntityModel<SateliteResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(SateliteController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(SateliteController.class).atualizarSatelite(response.id(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(SateliteController.class).deletarSatelite(response.id())).withRel("deletar"));
        resource.add(linkTo(methodOn(SateliteController.class).listarTodos(0, 10, null)).withRel("todos-satelites"));
        return resource;
    }
}