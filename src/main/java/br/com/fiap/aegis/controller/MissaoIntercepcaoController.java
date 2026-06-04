package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.MissaoRequestDTO;
import br.com.fiap.aegis.dto.MissaoResponseDTO;
import br.com.fiap.aegis.service.MissaoIntercepcaoService;
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
@RequestMapping("/api/missoes")
@Tag(name = "Missões de Intercepção", description = "Endpoints para controlo, despacho e monitorização de drones de limpeza orbital")
public class MissaoIntercepcaoController {

    @Autowired
    private MissaoIntercepcaoService missaoService;

    @PostMapping("/despachar")
    @Operation(summary = "Despachar um drone Chaser", description = "Autoriza o envio de um drone de limpeza para interceptar uma ameaça gerando links para chaves compostas")
    public ResponseEntity<EntityModel<MissaoResponseDTO>> despacharDrone(@Valid @RequestBody MissaoRequestDTO dto) {
        MissaoResponseDTO response = missaoService.despacharDrone(dto);

        EntityModel<MissaoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(MissaoIntercepcaoController.class)
                .buscarPorIdComposto(response.droneId(), response.detritoId())).withSelfRel());
        resource.add(linkTo(methodOn(MissaoIntercepcaoController.class).listarTodas()).withRel("todas-missoes"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{droneId}/{detritoId}")
    @Operation(summary = "Buscar Missão por ID Composto", description = "Retorna os detalhes de uma missão através do ID do Drone e do Detrito")
    public ResponseEntity<EntityModel<MissaoResponseDTO>> buscarPorIdComposto(@PathVariable Long droneId, @PathVariable Long detritoId) {
        MissaoResponseDTO response = missaoService.buscarPorIdComposto(droneId, detritoId);

        EntityModel<MissaoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(MissaoIntercepcaoController.class).buscarPorIdComposto(droneId, detritoId)).withSelfRel());
        resource.add(linkTo(methodOn(MissaoIntercepcaoController.class).listarTodas()).withRel("todas-missoes"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todas as missões orbitais", description = "Retorna a listagem completa injetando metadados de navegação HATEOAS")
    public ResponseEntity<CollectionModel<EntityModel<MissaoResponseDTO>>> listarTodas() {
        List<EntityModel<MissaoResponseDTO>> missoes = missaoService.listarTodas().stream()
                .map(missao -> EntityModel.of(missao,
                        linkTo(methodOn(MissaoIntercepcaoController.class).buscarPorIdComposto(missao.droneId(), missao.detritoId())).withSelfRel(),
                        linkTo(methodOn(MissaoIntercepcaoController.class).listarTodas()).withRel("todas-missoes")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<MissaoResponseDTO>> collectionModel = CollectionModel.of(missoes);
        collectionModel.add(linkTo(methodOn(MissaoIntercepcaoController.class).listarTodas()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}