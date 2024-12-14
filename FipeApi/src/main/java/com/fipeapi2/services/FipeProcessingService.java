package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import com.fipeapi1.services.FipeClient;
import io.smallrye.reactive.messaging.annotations.Merge;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    VeiculoRepository veiculoRepository;

    @Merge
    @Incoming("marcas-da-api1")
    public void processarMarca(Map<String, String> marca) {
        // Obter o código da marca
        String codigoMarca = marca.get("codigo");

        // Buscar os modelos dessa marca utilizando a API FIPE
        Map<String, Object> modelosResponse = fipeClient.obterModelos(codigoMarca);

        // Verificar se a resposta contém a chave "modelos"
        if (modelosResponse != null && modelosResponse.containsKey("modelos")) {
            // Obter a lista de modelos
            List<Map<String, String>> modelos = (List<Map<String, String>>) modelosResponse.get("modelos");

            // Processa os modelos e armazena no banco
            for (Map<String, String> modelo : modelos) {
                Veiculo veiculo = new Veiculo();
                veiculo.setMarca(marca.get("nome"));  // Nome da marca
                veiculo.setCodigo(modelo.get("codigo"));  // Código do modelo
                veiculo.setModelo(modelo.get("nome"));  // Nome do modelo

                // Salva no banco de dados
                veiculoRepository.persist(veiculo);
            }
        } else {
            // Caso não haja modelos, você pode tratar o erro de forma adequada
            System.out.println("Nenhum modelo encontrado para a marca: " + marca.get("nome"));
        }
    }
}
