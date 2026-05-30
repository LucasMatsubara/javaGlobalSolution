package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.enums.StatusMissao;
import br.com.fiap.aegis.model.MissaoId;
import br.com.fiap.aegis.model.MissaoIntercepcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MissaoIntercepcaoRepository extends JpaRepository<MissaoIntercepcao, MissaoId> {
    List<MissaoIntercepcao> findByStatusMissao(StatusMissao statusMissao);
    List<MissaoIntercepcao> findByDroneId(Long droneId);
}
