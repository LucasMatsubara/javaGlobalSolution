package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.EmpresaRequestDTO;
import br.com.fiap.aegis.dto.EmpresaResponseDTO;
import br.com.fiap.aegis.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> cadastrarEmpresa(@Valid @RequestBody EmpresaRequestDTO dto) {
        EmpresaResponseDTO response = empresaService.cadastrarEmpresa(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> listarTodas() {
        List<EmpresaResponseDTO> response = empresaService.listarTodas();
        return ResponseEntity.ok(response);
    }
}
