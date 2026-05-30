package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DetritoRequestDTO;
import br.com.fiap.aegis.dto.DetritoResponseDTO;
import br.com.fiap.aegis.service.DetritoEspacialService;
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
@RequestMapping("/api/detritos")
@Tag(name = "Detritos Espaciais", description = "Endpoints para catalogação e rastreamento de lixo espacial e seus níveis de risco")
public class DetritoEspacialController {

    @Autowired
    private DetritoEspacialService detritoService;

    @PostMapping
    @Operation(summary = "Registrar novo detrito espacial", description = "Cataloga um novo fragmento de lixo espacial detectado pelos sistemas de radar")
    public ResponseEntity<EntityModel<DetritoResponseDTO>> registrarDetrito(@Valid @RequestBody DetritoRequestDTO dto) {
        DetritoResponseDTO response = detritoService.registrarDetrito(dto);

        EntityModel<DetritoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DetritoEspacialController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(DetritoEspacialController.class).listarTodos()).withRel("todos-detritos"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detrito por ID", description = "Retorna as coordenadas orbitais e o nível de risco de um detrito específico")
    public ResponseEntity<EntityModel<DetritoResponseDTO>> buscarPorId(@PathVariable Long id) {
        DetritoResponseDTO response = detritoService.buscarPorId(id);

        EntityModel<DetritoResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DetritoEspacialController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(DetritoEspacialController.class).listarTodos()).withRel("todos-detritos"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todos os detritos catalogados", description = "Retorna a lista completa de lixo espacial sob monitoramento na órbita terrestre")
    public ResponseEntity<CollectionModel<EntityModel<DetritoResponseDTO>>> listarTodos() {
        List<EntityModel<DetritoResponseDTO>> detritos = detritoService.listarTodos().stream()
                .map(detrito -> EntityModel.of(detrito,
                        linkTo(methodOn(DetritoEspacialController.class).buscarPorId(detrito.id())).withSelfRel(),
                        linkTo(methodOn(DetritoEspacialController.class).listarTodos()).withRel("todos-detritos")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<DetritoResponseDTO>> collectionModel = CollectionModel.of(detritos);
        collectionModel.add(linkTo(methodOn(DetritoEspacialController.class).listarTodos()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}