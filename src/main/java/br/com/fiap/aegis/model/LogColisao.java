package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_COLISAO")
@Data
public class LogColisao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // vincula qual satélite comercial está sob ameaça
    @ManyToOne
    @JoinColumn(name = "satelite_id", nullable = false)
    private Satelite satelite;

    // vincula qual detrito espacial gerou o alerta de risco
    @ManyToOne
    @JoinColumn(name = "detrito_id", nullable = false)
    private DetritoEspacial detrito;

    @Column(nullable = false)
    private String descricaoAlerta; // ex: "aproximação crítica detectada a menos de 500m"

    @Column(nullable = false)
    private LocalDateTime dataRegistro;
}
