package br.com.fiap.aegis.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class ObjetoOrbital {

    @Column(name = "eixox", nullable = false)
    private Double eixoX;

    @Column(name = "eixoy", nullable = false)
    private Double eixoY;

    @Column(name = "altitude", nullable = false)
    private Double altitude;
}