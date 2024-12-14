package com.fipeapi1.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Map;

@Path("/fipe")
@RegisterRestClient(baseUri = "https://parallelum.com.br/fipe/api/v1")
public interface FipeClient {

    // Endpoint para obter as marcas dos veículos
    @GET
    @Path("/carros/marcas")
    List<Map<String, String>> obterMarcas();

    // Endpoint para obter os modelos de um veículo a partir da marca
    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos")
    Map<String, Object> obterModelos(@PathParam("codigoMarca") String codigoMarca);

    // Endpoint para obter os anos de um modelo específico
    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos/{codigoModelo}/anos")
    List<Map<String, String>> obterAnos(@PathParam("codigoMarca") String codigoMarca,
                                        @PathParam("codigoModelo") String codigoModelo);

    // Endpoint para obter o preço de um modelo, ano e marca
    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos/{codigoModelo}/anos/{codigoAno}")
    Map<String, Object> obterPreco(@PathParam("codigoMarca") String codigoMarca,
                                   @PathParam("codigoModelo") String codigoModelo,
                                   @PathParam("codigoAno") String codigoAno);
}
