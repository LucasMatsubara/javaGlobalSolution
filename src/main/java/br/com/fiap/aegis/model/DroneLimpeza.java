package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusOperacional;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_DRONE_LIMPEZA")
@Data
@EqualsAndHashCode(callSuper = true)
public class DroneLimpeza extends ObjetoOrbital {

    private Double nivelBateria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusOperacional statusOperacional;
}
