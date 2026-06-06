package br.com.fiap.aegis.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class ObjetoOrbital {

    @Column(name = "eixox", nullable = false)
    private Double eixoX;

    @Column(name = "eixoy", nullable = false)
    private Double eixoY;

    @Column(name = "altitude", nullable = false)
    private Double altitude;
}