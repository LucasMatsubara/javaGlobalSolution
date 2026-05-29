package br.com.fiap.aegis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_SATELITE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Satelite extends ObjetoOrbital {
    private String empresaProprietaria;
    private String tipoBanda;
}
