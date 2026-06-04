package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.LogOperacaoResponseDTO;
import br.com.fiap.aegis.service.LogOperacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/api/logs")
@Tag(name = "Histórico de Operações (Logs)", description = "Timeline paginada de atividades recentes")
public class LogOperacaoController {

    @Autowired
    private LogOperacaoService logService;

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
    @Operation(summary = "Listar todos os Logs com Paginação", description = "Retorna os logs divididos em páginas configuráveis via query params")
    public ResponseEntity<PagedModel<EntityModel<LogOperacaoResponseDTO>>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<LogOperacaoResponseDTO> pagedResourcesAssembler) { // Injetado diretamente aqui como parâmetro!

        Pageable pageable = PageRequest.of(page, size);
        Page<LogOperacaoResponseDTO> logsPaginados = logService.listarTodosPaginado(pageable);

        PagedModel<EntityModel<LogOperacaoResponseDTO>> pagedModel = pagedResourcesAssembler.toModel(logsPaginados,
                log -> EntityModel.of(log,
                        linkTo(methodOn(LogOperacaoController.class).buscarPorId(log.id())).withSelfRel()
                )
        );

        return ResponseEntity.ok(pagedModel);
    }
}