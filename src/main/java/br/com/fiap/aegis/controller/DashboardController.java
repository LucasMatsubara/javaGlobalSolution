package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DashboardResponseDTO;
import br.com.fiap.aegis.security.EmpresaResolver;
import br.com.fiap.aegis.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Indicadores de saúde orbital — filtrados pela empresa logada")
public class DashboardController {

    @Autowired private DashboardService dashboardService;
    @Autowired private EmpresaResolver empresaResolver;

    @GetMapping("/resumo")
    @Operation(summary = "Resumo da Empresa", description = "Retorna indicadores apenas com dados da empresa do usuário logado")
    public ResponseEntity<EntityModel<DashboardResponseDTO>> obterResumo() {
        // ✅ Extrai empresaId do JWT e passa ao service
        Long empresaId = empresaResolver.getEmpresaIdDoUsuarioLogado();
        DashboardResponseDTO response = dashboardService.obterResumoDashboard(empresaId);
        EntityModel<DashboardResponseDTO> resource = EntityModel.of(response);
        resource.add(linkTo(methodOn(DashboardController.class).obterResumo()).withSelfRel());
        return ResponseEntity.ok(resource);
    }
}