package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.Satelite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SateliteRepository extends JpaRepository<Satelite, Long> {

    // busca lista satélites de empresa específica
    List<Satelite> findByEmpresaProprietariaIgnoreCase(String empresaProprietaria);
}
