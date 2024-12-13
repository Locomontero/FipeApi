package com.fipeapi1.services;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/fipe")
public class FipeService {

    @Inject
    FipeClient fipeClient;  // Injeção do cliente Feign

    @GET
    @Path("/marcas")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> obterMarcas() {
        return fipeClient.obterMarcas();
    }
}
