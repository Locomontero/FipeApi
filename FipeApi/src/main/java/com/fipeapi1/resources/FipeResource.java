package com.fipeapi1.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.inject.Inject;

import com.fipeapi1.services.FipeService;

@Path("/fipe")
public class FipeResource {

    @Inject
    FipeService fipeService;

    @POST
    @Path("/carga-inicial")
    public Response acionarCargaInicial() {
        try {
            fipeService.obterMarcas();
            return Response.ok("Carga inicial dos veículos iniciada com sucesso!").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao iniciar a carga inicial: " + e.getMessage())
                    .build();
        }
    }
}
