package com.fipeapi2.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VeiculoDTO {

    @NotNull(message = "A marca não pode ser nula.")
    @Size(min = 2, max = 100, message = "A marca deve ter entre 2 e 100 caracteres.")
    private String marca;

    @NotNull(message = "O modelo não pode ser nulo.")
    @Size(min = 1, max = 100, message = "O modelo deve ter entre 1 e 100 caracteres.")
    private String modelo;

    @NotNull(message = "O código não pode ser nulo.")
    @Size(min = 1, max = 50, message = "O código deve ter entre 1 e 50 caracteres.")
    private String codigo;

    @NotNull(message = "O código não pode ser nulo.")
    @Size(max = 255, message = "Observações não podem ter mais de 255 caracteres.")
    private String observacoes;
}
