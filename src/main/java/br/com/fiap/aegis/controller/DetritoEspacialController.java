package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DetritoRequestDTO;
import br.com.fiap.aegis.dto.DetritoResponseDTO;
import br.com.fiap.aegis.security.EmpresaResolver;
import br.com.fiap.aegis.service.DetritoEspacialService;
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
@RequestMapping("/api/detritos")
@Tag(name = "Detritos Espaciais", description = "Endpoints paginados para auditoria e controle do radar de detritos")
public class DetritoEspacialController {

    @Autowired
    private DetritoEspacialService detritoService;

    // ✅ CORREÇÃO 2: injeta o resolver para saber qual empresa está logada
    @Autowired
    private EmpresaResolver empresaResolver;

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
    @Operation(summary = "Listar detritos — filtrado pela empresa do usuário logado")
    public ResponseEntity<PagedModel<EntityModel<DetritoResponseDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<DetritoResponseDTO> pagedResourcesAssembler) {

        Pageable pageable = PageRequest.of(page, size);
        Long empresaId = empresaResolver.getEmpresaIdDoUsuarioLogado();

        // ✅ CORREÇÃO 2: cada empresa só vê seus próprios detritos
        Page<DetritoResponseDTO> detritosPaginados = (empresaId != null)
                ? detritoService.listarPorEmpresa(empresaId, pageable)
                : detritoService.listarTodosPaginado(pageable);

        PagedModel<EntityModel<DetritoResponseDTO>> pagedModel =
                pagedResourcesAssembler.toModel(detritosPaginados, this::criarEntityModel);

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<DetritoResponseDTO> criarEntityModel(DetritoResponseDTO response) {
        EntityModel<DetritoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DetritoEspacialController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(DetritoEspacialController.class).atualizarDetrito(response.id(), null)).withRel("atualizar"));
        resource.add(linkTo(methodOn(DetritoEspacialController.class).deletarDetrito(response.id())).withRel("deletar"));
        resource.add(linkTo(methodOn(DetritoEspacialController.class).listarTodos(0, 10, null)).withRel("todos-detritos"));
        return resource;
    }
}