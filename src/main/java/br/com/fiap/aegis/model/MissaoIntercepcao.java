package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusMissao;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_MISSAO_INTERCEPCAO")
@Data
public class MissaoIntercepcao {

    @EmbeddedId
    private MissaoId id;

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
    private StatusMissao statusMissao;
}