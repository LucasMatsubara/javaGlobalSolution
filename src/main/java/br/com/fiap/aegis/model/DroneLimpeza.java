package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusOperacional;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_DRONE_LIMPEZA")
@Data
public class DroneLimpeza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // bateria começando em 100%
    private Double nivelBateria = 100.0;

    @Enumerated(EnumType.STRING)
    private StatusOperacional statusOperacional = StatusOperacional.NA_BASE;
}
