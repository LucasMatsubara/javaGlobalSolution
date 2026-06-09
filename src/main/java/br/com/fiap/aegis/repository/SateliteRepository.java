package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.enums.StatusSatelite;
import br.com.fiap.aegis.model.Satelite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SateliteRepository extends JpaRepository<Satelite, Long> {
    List<Satelite> findByEmpresaNomeIgnoreCase(String empresaProprietaria);
    long countByStatusSatelite(StatusSatelite status);

    // Filtro por empresa (para isolamento de dados por empresa logada)
    Page<Satelite> findByEmpresaId(Long empresaId, Pageable pageable);
    long countByEmpresaIdAndStatusSatelite(Long empresaId, StatusSatelite status);
}