package br.com.fiap.aegis.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Captura a nossa exceção de "Não Encontrado" (Erro 404)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroPadraoDTO> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // 2. Captura os erros de Validação dos DTOs (Erro 400 - Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadraoDTO> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Pega todos os campos que falharam no @NotBlank, @NotNull, etc.
        List<String> errosDeValidacao = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Erro de validação nos dados enviados",
                request.getRequestURI(),
                errosDeValidacao
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // 3. Captura a nossa regra de negócio do Enum (Erro 400 - Ex: tentar despachar drone em manutenção)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroPadraoDTO> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        ErroPadraoDTO erro = new ErroPadraoDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                request.getRequestURI(),
                null
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
