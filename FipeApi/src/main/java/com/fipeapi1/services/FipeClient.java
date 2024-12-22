package com.fipeapi1.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/carros")
@RegisterRestClient
public interface FipeClient {

    @GET
    @Path("/marcas")
    String obterMarcas();

    @GET
    @Path("/carros/marcas/{codigoMarca}/modelos")
    ModelosResponse obterModelos(@PathParam("codigoMarca") String codigoMarca);


}
