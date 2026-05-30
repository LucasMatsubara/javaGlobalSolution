package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.EmpresaRequestDTO;
import br.com.fiap.aegis.dto.EmpresaResponseDTO;
import br.com.fiap.aegis.service.EmpresaService;
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
@RequestMapping("/api/empresas")
@Tag(name = "Empresas Aeroespaciais", description = "Endpoints para gestão das empresas proprietárias dos satélites")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    @Operation(summary = "Cadastrar nova Empresa", description = "Regista uma nova empresa aeroespacial na plataforma")
    public ResponseEntity<EntityModel<EmpresaResponseDTO>> cadastrarEmpresa(@Valid @RequestBody EmpresaRequestDTO dto) {
        EmpresaResponseDTO response = empresaService.cadastrarEmpresa(dto);

        EntityModel<EmpresaResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(EmpresaController.class).buscarPorId(response.id())).withSelfRel());
        resource.add(linkTo(methodOn(EmpresaController.class).listarTodas()).withRel("todas-empresas"));

        return ResponseEntity.status(HttpStatus.CREATED).body(resource);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Empresa por ID", description = "Retorna os detalhes de uma empresa específica")
    public ResponseEntity<EntityModel<EmpresaResponseDTO>> buscarPorId(@PathVariable Long id) {
        EmpresaResponseDTO response = empresaService.buscarPorId(id);

        EntityModel<EmpresaResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(EmpresaController.class).buscarPorId(id)).withSelfRel());
        resource.add(linkTo(methodOn(EmpresaController.class).listarTodas()).withRel("todas-empresas"));

        return ResponseEntity.ok(resource);
    }

    @GetMapping
    @Operation(summary = "Listar todas as Empresas", description = "Retorna o catálogo completo de empresas cadastradas")
    public ResponseEntity<CollectionModel<EntityModel<EmpresaResponseDTO>>> listarTodas() {
        List<EntityModel<EmpresaResponseDTO>> empresas = empresaService.listarTodas().stream()
                .map(empresa -> EntityModel.of(empresa,
                        linkTo(methodOn(EmpresaController.class).buscarPorId(empresa.id())).withSelfRel(),
                        linkTo(methodOn(EmpresaController.class).listarTodas()).withRel("todas-empresas")))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<EmpresaResponseDTO>> collectionModel = CollectionModel.of(empresas);
        collectionModel.add(linkTo(methodOn(EmpresaController.class).listarTodas()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }
}
