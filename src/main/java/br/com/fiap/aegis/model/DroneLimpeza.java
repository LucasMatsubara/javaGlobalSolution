package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusOperacional;
import br.com.fiap.aegis.enums.TipoBanda;
import br.com.fiap.aegis.model.Empresa;
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

    private Double nivelBateria = 100.0;

    @Enumerated(EnumType.STRING)
    private StatusOperacional statusOperacional = StatusOperacional.NA_BASE;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_banda")
    private TipoBanda tipoBanda = TipoBanda.BANDA_KA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}