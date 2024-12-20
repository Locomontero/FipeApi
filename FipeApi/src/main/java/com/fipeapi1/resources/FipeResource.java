package com.fipeapi1.resources;

import com.fipeapi1.services.FipeService;
import com.fipeapi2.entities.Veiculo;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/fipe")
public class FipeResource {

    @Inject
    FipeService fipeService;

    @POST
    @Path("/carga-inicial")
    public Response cargaInicial() {
        try {
            fipeService.carregarVeiculos();
            return Response.status(Response.Status.ACCEPTED)
                    .entity("Carga inicial de marcas iniciada. Processamento em andamento.")
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao iniciar carga inicial: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/marcas")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeiculos() {
        List<Veiculo> veiculos = fipeService.getTodosVeiculos();
        if (veiculos.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Nenhum veículo encontrado.")
                    .build();
        }
        return Response.ok(veiculos).build();
    }

    @GET
    @Path("/veiculos/marca/{marca}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeiculoByMarca(@PathParam("marca") String marca) {
        try {
            List<Veiculo> veiculos = fipeService.getVeiculoByMarca(marca);

            return Response.ok(veiculos).build();
        } catch (NotFoundException e) {

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/alterar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterarVeiculo(Veiculo veiculo) {

        Veiculo veiculoAlterado = fipeService.alterarVeiculo(veiculo);

        if (veiculoAlterado == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Veículo não encontrado.")
                    .build();
        }
        return Response.ok(veiculoAlterado).build();
    }

    @PUT
    @Path("/veiculo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeiculoByCodigo(@PathParam("codigo") String codigo) {
        try {
            Veiculo veiculo = fipeService.getVeiculoByCodigo(codigo);

            return Response.ok(veiculo).build();
        } catch (NotFoundException e) {

            return Response.status(Response.Status.NOT_FOUND)
                    .entity(e.getMessage())
                    .build();
        }
    }
}
