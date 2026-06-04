package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusSatelite;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "TB_SATELITE")
@Data
public class Satelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String noradId;
    private Double inclinacao;
    private LocalDate dataLancamento;

    @Enumerated(EnumType.STRING)
    private StatusSatelite statusSatelite = StatusSatelite.ATIVO;

    private Double massaKg;
    private String tipoBanda;

    @Embedded
    private CoordenadaOrbital coordenadas;

    // N:1 (vários satélites pertencem a uma única empresa)
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}

