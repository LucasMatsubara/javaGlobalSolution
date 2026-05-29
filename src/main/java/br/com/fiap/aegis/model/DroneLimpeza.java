package br.com.fiap.aegis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "TB_DRONE_LIMPEZA")
@Data
@EqualsAndHashCode(callSuper = true)
public class DroneLimpeza extends ObjetoOrbital {
    private Double nivelBateria;
    private String statusOperacional; // enum: EM_BASE, EM_MISSAO, INATIVO
}
