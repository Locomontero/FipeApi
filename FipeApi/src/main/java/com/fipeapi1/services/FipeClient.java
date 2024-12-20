package com.fipeapi1.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.Map;

@Path("/carros")
@RegisterRestClient
public interface FipeClient {

    @GET
    @Path("/marcas")
    String obterMarcas();

    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos")
    List<Map<String, Object>> obterModelos(@PathParam("codigoMarca") String codigoMarca);

}
