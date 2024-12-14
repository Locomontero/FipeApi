package com.fipeapi1.services;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;  // Injeção do cliente REST

    // Método para obter marcas
    public List<Map<String, String>> buscarMarcas() {
        return fipeClient.obterMarcas();  // Chama a API para obter marcas de carros
    }

    // Método para obter modelos a partir de uma marca
    public List<Map<String, String>> buscarModelos(String codigoMarca) {
        Map<String, Object> response = fipeClient.obterModelos(codigoMarca);  // Chama a API para obter modelos
        return (List<Map<String, String>>) response.get("modelos");  // A resposta contém a lista de modelos
    }

    // Método para obter os anos de um modelo
    public List<Map<String, String>> buscarAnos(String codigoMarca, String codigoModelo) {
        return fipeClient.obterAnos(codigoMarca, codigoModelo);  // Chama a API para obter anos
    }

    // Método para obter o preço de um modelo
    public Map<String, Object> buscarPreco(String codigoMarca, String codigoModelo, String codigoAno) {
        return fipeClient.obterPreco(codigoMarca, codigoModelo, codigoAno);  // Chama a API para obter preço
    }
}
