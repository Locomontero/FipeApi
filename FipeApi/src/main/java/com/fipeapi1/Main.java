package com.fipeapi1;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

    public static void main(String[] args) {
        System.out.println("Iniciando a aplicação FipeApi...");

        // Aqui você pode configurar coisas que deseja inicializar antes da execução do Quarkus
        Quarkus.run(args);
    }
}
