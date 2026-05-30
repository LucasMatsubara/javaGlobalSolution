package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_SATELITE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Satelite extends ObjetoOrbital {

    private String tipoBanda;

    // N:1 (vários satélites pertencem a uma única empresa)
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
}
