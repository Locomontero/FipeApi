package com.fipeapi1.services;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/fipe")
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;  // Injeção do cliente Feign

    @GET
    @Path("/marcas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, String>> obterMarcas() {
        return fipeClient.obterMarcas();  // Chama a API FIPE para obter as marcas
    }

    @GET
    @Path("/modelos")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> obterModelos(String codigoMarca) {
        return fipeClient.obterModelos(codigoMarca);  // Chama a API FIPE para obter os modelos
    }

    @GET
    @Path("/anos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, String>> obterAnos(String codigoMarca, String codigoModelo) {
        return fipeClient.obterAnos(codigoMarca, codigoModelo);  // Chama a API FIPE para obter os anos
    }

    @GET
    @Path("/preco")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> obterPreco(String codigoMarca, String codigoModelo, String codigoAno) {
        return fipeClient.obterPreco(codigoMarca, codigoModelo, codigoAno);  // Chama a API FIPE para obter o preço
    }

    @Outgoing("marcas-da-api1")  // Fila para enviar as marcas para a API-2
    public List<Map<String, String>> buscarMarcasEFila() {
        // Busca as marcas da FIPE
        List<Map<String, String>> marcas = fipeClient.obterMarcas();
        // Envia as marcas para a fila
        return marcas;
    }
}
