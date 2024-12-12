package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Incoming("marcas-da-api1")
    public void processarMarcas(String marcasJson) {
        // Aqui você processa as marcas e os códigos/modelos
        Veiculo veiculo = new Veiculo();
        veiculo.setMarca("Marca Exemplo");
        veiculo.setModelo("Modelo Exemplo");
        veiculo.setCodigo("123456");

        // Salvar no banco
        veiculoRepository.salvarVeiculo(veiculo);
    }
}

