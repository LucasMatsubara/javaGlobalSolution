package br.com.fiap.aegis.repository;

import br.com.fiap.aegis.model.DroneLimpeza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneLimpezaRepository extends JpaRepository<DroneLimpeza, Long> {

    // painel de controle sabe quais drones estão livres para missão
    List<DroneLimpeza> findByStatusOperacional(String statusOperacional);

    // dispara alertas de manutenção para drones com bateria abaixo de 20%
    List<DroneLimpeza> findByNivelBateriaLessThan(Double nivelBateria);
}
