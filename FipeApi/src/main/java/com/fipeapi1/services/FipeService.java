package com.fipeapi1.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.fipeapi2.services.FipeProcessingService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    FipeProcessingService fipeProcessingService;

    public List<Map<String, String>> buscarMarcas() {
        return fipeClient.obterMarcas();  // Chama a API para obter marcas de carros
    }

    // Método para buscar modelos por código de marca
    public List<Map<String, String>> buscarModelos(String codigoMarca) {
        Map<String, Object> response = fipeClient.obterModelos(codigoMarca);  // Chama a API para obter modelos
        return (List<Map<String, String>>) response.get("modelos");  // A resposta contém a lista de modelos
    }

    // Método para buscar os anos de um modelo específico
    public List<Map<String, String>> buscarAnos(String codigoMarca, String codigoModelo) {
        return fipeClient.obterAnos(codigoMarca, codigoModelo);  // Chama a API para obter anos
    }

    // Método para buscar o preço de um modelo específico
    public Map<String, Object> buscarPreco(String codigoMarca, String codigoModelo, String codigoAno) {
        return fipeClient.obterPreco(codigoMarca, codigoModelo, codigoAno);  // Chama a API para obter preço
    }

    // Método que orquestra todo o processo de busca de dados e envio ao Kafka
    public void processarMarcas() {
        // Busca todas as marcas
        List<Map<String, String>> marcas = buscarMarcas();

        // Para cada marca, busca os modelos, anos e preço, e envia para o Kafka
        for (Map<String, String> marca : marcas) {
            String codigoMarca = marca.get("codigo");
            String nomeMarca = marca.get("nome");

            // Enviar para o Kafka (API-2) via FipeProcessingService
            fipeProcessingService.processarMarca(marca);  // O Kafka irá processar a marca e enviar modelos
        }
    }
}
