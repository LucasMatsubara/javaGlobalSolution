package br.com.fiap.aegis.dto;

import br.com.fiap.aegis.enums.TipoBanda;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DroneRequestDTO(

        @NotBlank(message = "O nome do Drone é obrigatório")
        String nome,

        @NotNull(message = "O tipo de banda de comunicação é obrigatório")
        TipoBanda tipoBanda
) {}