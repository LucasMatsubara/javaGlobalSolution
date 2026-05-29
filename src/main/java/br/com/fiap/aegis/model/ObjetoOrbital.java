package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "TB_OBJETO_ORBITAL")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
public abstract class ObjetoOrbital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Double massaKg;

    @Embedded
    private CoordenadaOrbital coordenadas;
}
