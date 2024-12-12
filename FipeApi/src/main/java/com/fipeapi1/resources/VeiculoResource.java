package com.fipeapi1.resources;

import com.fipeapi1.services.VeiculoService;
import com.oracle.svm.core.annotate.Inject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/veiculos")
public class VeiculoResource {

    @Inject
    VeiculoService veiculoService;

    @GET
    @Path("/{marca}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getVeiculosPorMarca(@PathParam("marca") String marca) {
        return veiculoService.buscarVeiculosPorMarca(marca);
    }
}
