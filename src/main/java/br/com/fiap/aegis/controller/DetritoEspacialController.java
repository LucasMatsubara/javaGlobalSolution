package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.DetritoRequestDTO;
import br.com.fiap.aegis.dto.DetritoResponseDTO;
import br.com.fiap.aegis.service.DetritoEspacialService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detritos")
@Tag(name = "Detritos Espaciais", description = "Endpoints para catalogação e rastreamento de lixo espacial e seus níveis de risco")
public class DetritoEspacialController {

    @Autowired
    private DetritoEspacialService detritoService;

    @PostMapping
    public ResponseEntity<DetritoResponseDTO> registrarDetrito(@Valid @RequestBody DetritoRequestDTO dto) {
        DetritoResponseDTO response = detritoService.registrarDetrito(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DetritoResponseDTO>> listarTodos() {
        List<DetritoResponseDTO> response = detritoService.listarTodos();
        return ResponseEntity.ok(response);
    }
}
