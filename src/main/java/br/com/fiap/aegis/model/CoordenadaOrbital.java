package br.com.fiap.aegis.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class CoordenadaOrbital {

    @Column(name = "eixox", nullable = false)
    private Double eixoX;

    @Column(name = "eixoy", nullable = false)
    private Double eixoY;

    @Column(name = "altitude", nullable = false)
    private Double altitude;
}