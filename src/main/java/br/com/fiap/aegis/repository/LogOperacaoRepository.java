package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.LogOperacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface LogOperacaoRepository extends JpaRepository<LogOperacao, Long> {
    Page<LogOperacao> findAllByOrderByDataHoraDesc(Pageable pageable);
    long countByDataHoraAfter(LocalDateTime dataInicioDoDia);

    // ✅ Filtros por empresa
    Page<LogOperacao> findByEmpresaIdOrderByDataHoraDesc(Long empresaId, Pageable pageable);
    long countByEmpresaIdAndDataHoraAfter(Long empresaId, LocalDateTime dataInicioDoDia);
    long countByEmpresaId(Long empresaId);
}