package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.UsuarioRequestDTO;
import br.com.fiap.aegis.dto.UsuarioResponseDTO;
import br.com.fiap.aegis.service.UsuarioService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Gestão de Utilizadores", description = "Endpoints para consulta, edição de perfil e encerramento de contas")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar perfil do utilizador por ID")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> buscarPorId(@PathVariable Long id) {
        UsuarioResponseDTO response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados cadastrais (Botão Editar Perfil)")
    public ResponseEntity<EntityModel<UsuarioResponseDTO>> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioRequestDTO dto) {
        UsuarioResponseDTO response = usuarioService.atualizarUsuario(id, dto);
        return ResponseEntity.ok(criarEntityModel(response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Encerrar conta (Botão Excluir Conta do App)")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        usuarioService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "Listar todos os utilizadores cadastrados (Painel Admin)")
    public ResponseEntity<PagedModel<EntityModel<UsuarioResponseDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<UsuarioResponseDTO> pagedResourcesAssembler) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UsuarioResponseDTO> usuariosPaginados = usuarioService.listarTodosPaginado(pageable);

        PagedModel<EntityModel<UsuarioResponseDTO>> pagedModel = pagedResourcesAssembler.toModel(usuariosPaginados,
                this::criarEntityModel
        );

        return ResponseEntity.ok(pagedModel);
    }

    private EntityModel<UsuarioResponseDTO> criarEntityModel(UsuarioResponseDTO response) {
        EntityModel<UsuarioResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(UsuarioController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(UsuarioController.class).atualizarUsuario(response.id(), null)).withRel("atualizar-perfil"));
        resource.add(linkTo(methodOn(UsuarioController.class).deletarUsuario(response.id())).withRel("encerrar-conta"));
        resource.add(linkTo(methodOn(UsuarioController.class).listarTodos(0, 10, null)).withRel("todos-usuarios"));
        return resource;
    }
}