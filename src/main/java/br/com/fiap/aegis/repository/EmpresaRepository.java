package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    // Busca empresa pelo CNPJ ou nome
    Empresa findByCnpj(String cnpj);
}