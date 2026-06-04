package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.LogOperacaoResponseDTO;
import br.com.fiap.aegis.service.LogOperacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/logs")
@Tag(name = "Histórico de Operações (Logs)", description = "Endpoints de auditoria para visualização da timeline de atividades recentes do sistema AEGIS")
public class LogOperacaoController {

    @Autowired
    private LogOperacaoService logService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Log por ID", description = "Retorna os detalhes técnicos de um registo de operação específico")
    public ResponseEntity<EntityModel<LogOperacaoResponseDTO>> buscarPorId(@PathVariable Long id) {
        LogOperacaoResponseDTO response = logService.buscarPorId(id);

        EntityModel<LogOperacaoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(LogOperacaoController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(LogOperacaoController.class).listarTodos()).withRel("todos-logs"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os Logs", description = "Retorna a cronologia completa de eventos do sistema (Atividade Recente) ordenada da mais recente para a mais antiga")
    public ResponseEntity<CollectionModel<EntityModel<LogOperacaoResponseDTO>>> listarTodos() {
        List<EntityModel<LogOperacaoResponseDTO>> logs = logService.listarTodos().stream()
                .map(log -> EntityModel.of(log,
                        linkTo(methodOn(LogOperacaoController.class).buscarPorId(log.id())).withSelfRel(),
                        linkTo(methodOn(LogOperacaoController.class).listarTodos()).withRel("todos-logs")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<LogOperacaoResponseDTO>> collectionModel = CollectionModel.of(logs);
        collectionModel.add(linkTo(methodOn(LogOperacaoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}