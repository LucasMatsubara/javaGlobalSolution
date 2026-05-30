package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.SateliteRequestDTO;
import br.com.fiap.aegis.dto.SateliteResponseDTO;
import br.com.fiap.aegis.service.SateliteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/satelites")
@Tag(name = "Satélites Comerciais", description = "Endpoints para cadastro e monitorização de satélites ativos em órbita que necessitam de proteção")
public class SateliteController {

    @Autowired
    private SateliteService sateliteService;

    @PostMapping
    public ResponseEntity<SateliteResponseDTO> cadastrarSatelite(@Valid @RequestBody SateliteRequestDTO dto) {
        SateliteResponseDTO response = sateliteService.cadastrarSatelite(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<SateliteResponseDTO>> listarTodos() {
        List<SateliteResponseDTO> response = sateliteService.listarTodos();
        return ResponseEntity.ok(response);
    }
}