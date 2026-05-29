package br.com.fiap.aegis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_DETRITO_ESPACIAL")
@Data
@EqualsAndHashCode(callSuper = true)
public class DetritoEspacial extends ObjetoOrbital {
    private String riscoColisao; // enum: ALTO, MEDIO, BAIXO
    private String origem; // ex1: Resto de Foguete
}
