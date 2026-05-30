package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.MissaoId;
import br.com.fiap.aegis.model.MissaoIntercepcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissaoIntercepcaoRepository extends JpaRepository<MissaoIntercepcao, MissaoId> {

    // missões em andamento ou finalizadas
    List<MissaoIntercepcao> findByStatusMissao(String statusMissao);

    // tela do Mobile lista o histórico de missões de um drone específico
    List<MissaoIntercepcao> findByDroneId(Long droneId);
}
