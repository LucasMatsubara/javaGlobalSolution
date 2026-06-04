package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.RiscoColisao;
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
    private Double velocidade;

    @Enumerated(EnumType.STRING)
    private RiscoColisao riscoColisao;

    private String origen;

    @Embedded
    private CoordenadaOrbital coordenadas;
}