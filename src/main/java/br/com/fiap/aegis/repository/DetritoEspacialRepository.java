package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.model.DetritoEspacial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetritoEspacialRepository extends JpaRepository<DetritoEspacial, Long> {
    List<DetritoEspacial> findByRiscoColisao(RiscoColisao riscoColisao);
    List<DetritoEspacial> findBySateliteId(Long sateliteId);
    long countByRiscoColisao(RiscoColisao risco);
    long countByEmpresaIdAndRiscoColisao(Long empresaId, RiscoColisao risco);

    Page<DetritoEspacial> findByEmpresaId(Long empresaId, Pageable pageable);
    long countByEmpresaId(Long empresaId);
}