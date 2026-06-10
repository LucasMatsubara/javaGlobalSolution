package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.RiscoColisao;
import br.com.fiap.aegis.enums.TipoDetrito;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tb_detrito_espacial")
public class DetritoEspacial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "massa_kg")
    private Double massaKg;

    @Column(name = "velocidade")
    private Double velocidade;

    @Enumerated(EnumType.STRING)
    @Column(name = "risco_colisao")
    private RiscoColisao riscoColisao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_detrito")
    private TipoDetrito tipoDetrito;

    @Column(name = "origem")
    private String origem;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "satelite_id")
    private Satelite satelite;

    @Embedded
    private CoordenadaOrbital coordenada;
}