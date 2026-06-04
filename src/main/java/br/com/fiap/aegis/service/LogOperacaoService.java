package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.LogOperacaoResponseDTO;
import br.com.fiap.aegis.exception.ResourceNotFoundException;
import br.com.fiap.aegis.model.LogOperacao;
import br.com.fiap.aegis.repository.LogOperacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public Page<LogOperacaoResponseDTO> listarTodosPaginado(Pageable pageable) {
        return logRepository.findAllByOrderByDataHoraDesc(pageable)
                .map(this::mapearParaResponseDTO);
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