package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.MissaoRequestDTO;
import br.com.fiap.aegis.dto.MissaoResponseDTO;
import br.com.fiap.aegis.service.MissaoIntercepcaoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/missoes")
@Tag(name = "Missões de Intercepção", description = "Endpoints para controlo, despacho e monitorização de drones de limpeza orbital")
public class MissaoIntercepcaoController {

    @Autowired
    private MissaoIntercepcaoService missaoService;

    @PostMapping("/despachar")
    public ResponseEntity<MissaoResponseDTO> despacharDrone(@Valid @RequestBody MissaoRequestDTO dto) {
        MissaoResponseDTO response = missaoService.despacharDrone(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
