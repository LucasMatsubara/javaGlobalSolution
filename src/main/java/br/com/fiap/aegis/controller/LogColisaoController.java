package br.com.fiap.aegis.controller;

import br.com.fiap.aegis.dto.LogColisaoResponseDTO;
import br.com.fiap.aegis.service.LogColisaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs-colisao")
public class LogColisaoController {

    @Autowired
    private LogColisaoService logColisaoService;

    @GetMapping
    public ResponseEntity<List<LogColisaoResponseDTO>> listarTodos() {
        List<LogColisaoResponseDTO> response = logColisaoService.listarTodos();
        return ResponseEntity.ok(response);
    }
}
