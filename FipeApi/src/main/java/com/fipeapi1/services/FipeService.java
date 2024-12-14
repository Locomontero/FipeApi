package com.fipeapi1.services;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@Path("/fipe")  // Endpoints internos ou públicos da sua aplicação
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;  // Injeção do cliente que fará a comunicação com a API FIPE

    // Expondo o endpoint /fipe/marcas que chama a API FIPE para obter as marcas
    @GET
    @Path("/marcas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, String>> obterMarcas() {
        return fipeClient.obterMarcas();  // Chama o cliente FIPE para obter as marcas de veículos
    }

    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> obterModelos(@PathParam("codigoMarca") String codigoMarca) {
        return fipeClient.obterModelos(codigoMarca);
    }


    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos/{codigoModelo}/anos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Map<String, String>> obterAnos(@PathParam("codigoMarca") String codigoMarca,
                                               @PathParam("codigoModelo") String codigoModelo) {
        return fipeClient.obterAnos(codigoMarca, codigoModelo);
    }

    @GET
    @Path("/marcas/{codigoMarca}/modelos/{codigoModelo}/anos/{codigoAno}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> obterPreco(@PathParam("codigoMarca") String codigoMarca,
                                          @PathParam("codigoModelo") String codigoModelo,
                                          @PathParam("codigoAno") String codigoAno) {
        return fipeClient.obterPreco(codigoMarca, codigoModelo, codigoAno);
    }


    @Outgoing("marcas-da-api1")  // Envia as marcas para a fila "marcas-da-api1"
    public List<Map<String, String>> buscarMarcasEFila() {
        // Busca as marcas da FIPE usando o cliente
        List<Map<String, String>> marcas = fipeClient.obterMarcas();
        // Envia as marcas para a fila
        return marcas;
    }

}
