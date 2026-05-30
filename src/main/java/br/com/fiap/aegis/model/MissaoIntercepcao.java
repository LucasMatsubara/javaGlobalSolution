package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_MISSAO_INTERCEPCAO")
@Data
public class MissaoIntercepcao {

    @EmbeddedId
    private MissaoId id = new MissaoId();

    @ManyToOne
    @MapsId("droneId")
    @JoinColumn(name = "drone_id")
    private DroneLimpeza drone;

    @ManyToOne
    @MapsId("detritoId")
    @JoinColumn(name = "detrito_id")
    private DetritoEspacial detrito;

    private LocalDateTime dataMissao;
    private String statusMissao; // enum: DESPACHADO, EM_APROXIMACAO, CAPTURADO
}
