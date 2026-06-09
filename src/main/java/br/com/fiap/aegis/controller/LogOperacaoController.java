package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.LogOperacaoResponseDTO;
import br.com.fiap.aegis.security.EmpresaResolver;
import br.com.fiap.aegis.service.LogOperacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Histórico de Operações (Logs)", description = "Timeline paginada — filtrada por empresa")
public class LogOperacaoController {

    @Autowired private LogOperacaoService logService;
    @Autowired private EmpresaResolver empresaResolver;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Log por ID")
    public ResponseEntity<EntityModel<LogOperacaoResponseDTO>> buscarPorId(@PathVariable Long id) {
        LogOperacaoResponseDTO response = logService.buscarPorId(id);
        EntityModel<LogOperacaoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(LogOperacaoController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(LogOperacaoController.class).listarTodos(0, 10, null)).withRel("todos-logs"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar Logs — filtrado pela empresa do usuário logado")
    public ResponseEntity<PagedModel<EntityModel<LogOperacaoResponseDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<LogOperacaoResponseDTO> pagedResourcesAssembler) {

        Pageable pageable = PageRequest.of(page, size);
        Long empresaId = empresaResolver.getEmpresaIdDoUsuarioLogado();

        // ✅ Cada empresa vê apenas suas próprias atividades
        Page<LogOperacaoResponseDTO> logsPaginados = (empresaId != null)
                ? logService.listarPorEmpresa(empresaId, pageable)
                : logService.listarTodosPaginado(pageable);

        PagedModel<EntityModel<LogOperacaoResponseDTO>> pagedModel = pagedResourcesAssembler.toModel(
                logsPaginados,
                log -> EntityModel.of(log,
                        linkTo(methodOn(LogOperacaoController.class).buscarPorId(log.id())).withSelfRel())
        );

        return ResponseEntity.ok(pagedModel);
    }
}