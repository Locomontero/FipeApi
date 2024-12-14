package com.fipeapi1.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Map;

@RegisterRestClient
@Path("/api/v1")
public interface FipeClient {

    @GET
    @Path("/carros/marcas")
    @Produces(MediaType.APPLICATION_JSON)
    List<Map<String, String>> obterMarcas();

    @GET
    @Path("/carros/marcas/{codigo}/modelos")
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> obterModelos(@QueryParam("codigo") String codigoMarca);

    @GET
    @Path("/carros/marcas/{codigo}/modelos/{codigoModelo}/anos")
    @Produces(MediaType.APPLICATION_JSON)
    List<Map<String, String>> obterAnos(@QueryParam("codigo") String codigoMarca,
                                        @QueryParam("codigoModelo") String codigoModelo);

    @GET
    @Path("/carros/marcas/{codigo}/modelos/{codigoModelo}/anos/{codigoAno}")
    @Produces(MediaType.APPLICATION_JSON)
    Map<String, Object> obterPreco(@QueryParam("codigo") String codigoMarca,
                                   @QueryParam("codigoModelo") String codigoModelo,
                                   @QueryParam("codigoAno") String codigoAno);
}
