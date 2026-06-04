package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DashboardResponseDTO;
import br.com.fiap.aegis.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Endpoint de agregação e estatísticas gerais para a tela inicial do sistema")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/resumo")
    @Operation(summary = "Obter Resumo Geral", description = "Retorna os indicadores calculados de saúde orbital, frotas e satélites")
    public ResponseEntity<EntityModel<DashboardResponseDTO>> obterResumo() {
        DashboardResponseDTO response = dashboardService.obterResumoDashboard();

        EntityModel<DashboardResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DashboardController.class).obterResumo()).withSelfRel());

        return ResponseEntity.ok(resource);
    }
}