package br.com.fiap.aegis.service;

import br.com.fiap.aegis.dto.LogColisaoResponseDTO;
import br.com.fiap.aegis.model.DetritoEspacial;
import br.com.fiap.aegis.model.LogColisao;
import br.com.fiap.aegis.model.Satelite;
import br.com.fiap.aegis.repository.LogColisaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogColisaoService {

    @Autowired
    private LogColisaoRepository logColisaoRepository;

    // método interno usado pelo sistema (ex: rotinas automatizadas)
    public LogColisaoResponseDTO registrarAlerta(Satelite satelite, DetritoEspacial detrito, String descricao) {
        LogColisao log = new LogColisao();
        log.setSatelite(satelite);
        log.setDetrito(detrito);
        log.setDescricaoAlerta(descricao);
        log.setDataRegistro(LocalDateTime.now());

        LogColisao logSalvo = logColisaoRepository.save(log);

        return mapearParaResponseDTO(logSalvo);
    }

    // mobile lista os alertas no dashboard
    public List<LogColisaoResponseDTO> listarTodos() {
        return logColisaoRepository.findAll().stream()
                .map(this::mapearParaResponseDTO)
                .collect(Collectors.toList());
    }

    private LogColisaoResponseDTO mapearParaResponseDTO(LogColisao log) {
        return new LogColisaoResponseDTO(
                log.getId(),
                log.getSatelite().getNome(),
                log.getDetrito().getNome(),
                log.getDescricaoAlerta(),
                log.getDataRegistro()
        );
    }
}
