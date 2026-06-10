package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Empresa findByCnpj(String cnpj);

    Long Id(Long id);

    List<Empresa> id(Long id);
}