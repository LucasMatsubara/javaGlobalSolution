package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.TipoBanda;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_SATELITE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Satelite extends ObjetoOrbital {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoBanda tipoBanda;

    // N:1 (vários satélites pertencem a uma única empresa)
    @ManyToOne
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;
}