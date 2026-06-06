package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.RiscoColisao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @Column(name = "risco_colisao")
    private RiscoColisao riscoColisao;

    @Column(name = "origem")
    private String origem;

    @Embedded
    private CoordenadaOrbital coordenada;
}