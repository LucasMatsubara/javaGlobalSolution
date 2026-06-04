package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.LogOperacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogOperacaoRepository extends JpaRepository<LogOperacao, Long> {
    // buscar os logs mais recentes primeiro
    Page<LogOperacao> findAllByOrderByDataHoraDesc(Pageable pageable);
    long countByDataHoraAfter(LocalDateTime dataInicioDoDia);
}
