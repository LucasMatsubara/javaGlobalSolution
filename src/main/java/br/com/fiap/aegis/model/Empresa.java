package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "TB_EMPRESA")
@Data
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    // 1:N (uma empresa pode ter vários satélites cadastrados)
    // cascade garante que operações na empresa se reflitam nos seus satélites, se necessário
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Satelite> satelites;
}