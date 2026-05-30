package br.com.fiap.aegis.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroPadraoDTO(
        LocalDateTime timestamp,
        Integer status,
        String erro,
        String caminho,
        List<String> detalhes // lista os erros de validação (@NotBlank, etc)
) {}
