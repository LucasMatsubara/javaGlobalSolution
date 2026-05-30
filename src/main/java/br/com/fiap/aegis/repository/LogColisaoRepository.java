package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.LogColisao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogColisaoRepository extends JpaRepository<LogColisao, Long> {
    // Para o frontend listar os alertas das últimas 24 horas, por exemplo
    List<LogColisao> findByDataRegistroAfter(LocalDateTime data);
}
