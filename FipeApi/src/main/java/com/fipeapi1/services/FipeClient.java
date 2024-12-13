package com.fipeapi1.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RegisterRestClient
@Path("/api/v1/marcas")
public interface FipeClient {

    // End-point para obter as marcas
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<String> obterMarcas();
}
