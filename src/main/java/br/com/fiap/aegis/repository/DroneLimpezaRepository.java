package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.model.DroneLimpeza;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DroneLimpezaRepository extends JpaRepository<DroneLimpeza, Long> {
    List<DroneLimpeza> findByStatusOperacional(StatusOperacional statusOperacional);
    List<DroneLimpeza> findByNivelBateriaLessThan(Double nivelBateria);
    long countByStatusOperacional(StatusOperacional status);
    long countByEmpresaIdAndStatusOperacional(Long empresaId, StatusOperacional status);

    // Filtro por empresa
    Page<DroneLimpeza> findByEmpresaId(Long empresaId, Pageable pageable);
    List<DroneLimpeza> findByEmpresaIdAndStatusOperacional(Long empresaId, StatusOperacional statusOperacional);
}