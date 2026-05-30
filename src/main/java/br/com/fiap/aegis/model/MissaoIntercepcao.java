package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusMissao;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusMissao statusMissao;
}