package com.fipeapi1.resources;

import com.fipeapi1.services.VeiculoService;
import com.fipeapi2.entities.Veiculo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/marcas")
public class MarcaResource {

    @Inject
    VeiculoService veiculoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getMarcas() {
        return veiculoService.buscarMarcas();
    }
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void atualizarVeiculo(@PathParam("id") Long id, Veiculo veiculo) {
        veiculoService.atualizarVeiculo(id, veiculo);
    }


}
