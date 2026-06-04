package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.LogOperacaoResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.LogOperacao;
import br.com.fiap.aegis.repository.LogOperacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogOperacaoService {

    @Autowired
    private LogOperacaoRepository logRepository;

    public void registarAcao(String alvo, String descricao, String gravidade) {
        LogOperacao log = new LogOperacao();
        log.setEntidadeAlvo(alvo);
        log.setDescricao(descricao);
        log.setNivelGravidade(gravidade);
        log.setDataHora(LocalDateTime.now());
        logRepository.save(log);
    }

    public List<LogOperacaoResponseDTO> listarTodos() {
        return logRepository.findAllByOrderByDataHoraDesc().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
    }

    public LogOperacaoResponseDTO buscarPorId(Long id) {
        LogOperacao log = logRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Log não encontrado com o ID: " + id));
        return mapearParaResponseDTO(log);
    }

    private LogOperacaoResponseDTO mapearParaResponseDTO(LogOperacao log) {
        return new LogOperacaoResponseDTO(
                log.getId(),
                log.getEntidadeAlvo(),
                log.getDescricao(),
                log.getNivelGravidade(),
                log.getDataHora()
        );
    }
}