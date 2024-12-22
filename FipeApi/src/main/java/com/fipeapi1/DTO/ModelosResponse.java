package com.fipeapi1.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelosResponse {

    @JsonProperty("modelos")
    private List<Modelo> modelos;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Modelo {
        private int codigo;
        private String nome;
    }
}
