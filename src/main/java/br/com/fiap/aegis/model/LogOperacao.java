package br.com.fiap.aegis.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "TB_LOG_OPERACAO")
@Data
public class LogOperacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // alvo da operação? Ex: Fragmento de Foguete, aegis
    private String entidadeAlvo;

    // o que aconteceu? Ex: Nova unidade de interceptação fabricada, Ameaça neutralizada
    private String descricao;

    // colorir as bolinhas no front-end: CRITICO, MODERADO, SISTEMA, INFO
    private String nivelGravidade;

    private LocalDateTime dataHora;
}