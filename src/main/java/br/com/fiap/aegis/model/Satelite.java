package br.com.fiap.aegis.model;

import br.com.fiap.aegis.enums.StatusSatelite;
import br.com.fiap.aegis.enums.TipoBanda;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tb_satelite")
public class Satelite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "norad_id", nullable = false, unique = true)
    private Long noradId;

    @Column(name = "inclinacao", nullable = false)
    private Double inclinacao;

    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;

    @Column(name = "status_satelite")
    private StatusSatelite statusSatelite;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_banda")
    private TipoBanda tipoBanda;

    @Column(name = "massa_kg")
    private Double massaKg;

    @Embedded
    private CoordenadaOrbital coordenada;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}