package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import com.fipeapi2.clients.FipeClient;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    FipeClient fipeClient;

    @Inject
    VeiculoRepository veiculoRepository;  // Repositório para salvar os veículos no banco de dados

    @Incoming("marcas-da-api1")  // Consumir a fila com as marcas
    public void processarMarca(Map<String, String> marca) {
        // Buscar os modelos dessa marca
        String codigoMarca = marca.get("codigo");
        var modelos = fipeClient.obterModelos(codigoMarca);

        // Processa os modelos e armazena no banco
        for (Map<String, String> modelo : (List<Map<String, String>>) modelos.get("modelos")) {
            Veiculo veiculo = new Veiculo();
            veiculo.setMarca(marca.get("nome"));
            veiculo.setCodigo(modelo.get("codigo"));
            veiculo.setModelo(modelo.get("nome"));

            // Salva no banco de dados
            veiculoRepository.persist(veiculo);
        }
    }
}
