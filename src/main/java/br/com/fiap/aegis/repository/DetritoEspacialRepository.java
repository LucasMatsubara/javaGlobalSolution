package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.model.DetritoEspacial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetritoEspacialRepository extends JpaRepository<DetritoEspacial, Long> {
    List<DetritoEspacial> findByRiscoColisao(RiscoColisao riscoColisao);
}