package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.RiscoColisao;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_DETRITO_ESPACIAL")
@Data
@EqualsAndHashCode(callSuper = true)
public class DetritoEspacial extends ObjetoOrbital {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiscoColisao riscoColisao;

    private String origem;
}