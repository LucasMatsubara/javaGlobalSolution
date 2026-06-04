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
@RequestMapping("/api/logs-colisao")
@Tag(name = "Logs de Colisão (Alertas)", description = "Endpoints de auditoria para visualização de aproximações perigosas e rotas críticas detetadas")
public class LogOperacaoController {

    @Autowired
    private LogOperacaoService logColisaoService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Alerta por ID", description = "Retorna os detalhes de um alerta de colisão específico")
    public ResponseEntity<EntityModel<LogOperacaoResponseDTO>> buscarPorId(@PathVariable Long id) {
        LogOperacaoResponseDTO response = logColisaoService.buscarPorId(id);

        EntityModel<LogOperacaoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(LogOperacaoController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(LogOperacaoController.class).listarTodos()).withRel("todos-logs"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os Alertas", description = "Retorna o painel completo de alertas e riscos de colisão registados pelo sistema")
    public ResponseEntity<CollectionModel<EntityModel<LogOperacaoResponseDTO>>> listarTodos() {
        List<EntityModel<LogOperacaoResponseDTO>> logs = logColisaoService.listarTodos().stream()
                .map(log -> EntityModel.of(log,
                        linkTo(methodOn(LogOperacaoController.class).buscarPorId(log.id())).withSelfRel(),
                        linkTo(methodOn(LogOperacaoController.class).listarTodos()).withRel("todos-logs")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<LogOperacaoResponseDTO>> collectionModel = CollectionModel.of(logs);
        collectionModel.add(linkTo(methodOn(LogOperacaoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}