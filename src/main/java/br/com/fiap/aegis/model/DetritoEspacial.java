package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_DETRITO_ESPACIAL")
@Data
public class DetritoEspacial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double massaKg;

    // velocidade em km/s
    private Double velocidade;

    private String riscoColisao;
    private String origem;

    @Embedded
    private CoordenadaOrbital coordenadas;
}