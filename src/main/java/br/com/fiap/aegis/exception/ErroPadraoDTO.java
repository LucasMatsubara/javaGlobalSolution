package br.com.fiap.aegis.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErroPadraoDTO(
        LocalDateTime timestamp,
        Integer status,
        String message,
        String path,
        List<String> details // Lista os erros de validação (@NotBlank, @NotNull) de forma limpa
) {}