package com.exceptions;

import lombok.Getter;

@Getter
public class VeiculosNotFoundException extends RuntimeException {

    private final String marca;

    public VeiculosNotFoundException(String marca) {
        super("Veículos com a marca '" + marca + "' não encontrados.");
        this.marca = marca;
    }
}