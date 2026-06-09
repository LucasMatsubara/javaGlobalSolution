package br.com.fiap.aegis.model;

import br.com.fiap.aegis.model.Empresa;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "TB_LOG_OPERACAO")
@Data
public class LogOperacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entidadeAlvo;
    private String descricao;
    private String nivelGravidade;
    private LocalDateTime dataHora;

    // ✅ Vincula o log à empresa
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;
}