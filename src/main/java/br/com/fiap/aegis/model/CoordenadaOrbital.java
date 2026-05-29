package br.com.fiap.aegis.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class CoordenadaOrbital {
    private Double eixoX;
    private Double eixoY;
    private Double eixoZ;
}
