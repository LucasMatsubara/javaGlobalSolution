package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.LogColisaoResponseDTO;
import br.com.fiap.aegis.service.LogColisaoService;
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
public class LogColisaoController {

    @Autowired
    private LogColisaoService logColisaoService;

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Alerta por ID", description = "Retorna os detalhes de um alerta de colisão específico")
    public ResponseEntity<EntityModel<LogColisaoResponseDTO>> buscarPorId(@PathVariable Long id) {
        LogColisaoResponseDTO response = logColisaoService.buscarPorId(id);

        EntityModel<LogColisaoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(LogColisaoController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(LogColisaoController.class).listarTodos()).withRel("todos-logs"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os Alertas", description = "Retorna o painel completo de alertas e riscos de colisão registados pelo sistema")
    public ResponseEntity<CollectionModel<EntityModel<LogColisaoResponseDTO>>> listarTodos() {
        List<EntityModel<LogColisaoResponseDTO>> logs = logColisaoService.listarTodos().stream()
                .map(log -> EntityModel.of(log,
                        linkTo(methodOn(LogColisaoController.class).buscarPorId(log.id())).withSelfRel(),
                        linkTo(methodOn(LogColisaoController.class).listarTodos()).withRel("todos-logs")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<LogColisaoResponseDTO>> collectionModel = CollectionModel.of(logs);
        collectionModel.add(linkTo(methodOn(LogColisaoController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}