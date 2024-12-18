package com.fipeapi1.resources;

import com.fipeapi1.services.FipeService;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/fipe")
public class FipeResource {

    @Inject
    FipeService fipeService;

    @Inject
    VeiculoRepository veiculoRepository;

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

        List<Veiculo> veiculos = veiculoRepository.listAll();
        if (veiculos.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Nenhum veículo encontrado.")
                    .build();
        }

        return Response.ok(veiculos).build();
    }

    @GET
    @Path("/modelos/{marca}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModelosPorMarca(@PathParam("marca") String marca) {

        List<Veiculo> modelos = veiculoRepository.find("marca", marca).list();
        if (modelos.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Nenhum modelo encontrado para a marca: " + marca)
                    .build();
        }
        return Response.ok(modelos).build();
    }

    @POST
    @Path("/alterar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterarVeiculo(Veiculo veiculo) {
        // Procura o veículo pelo ID
        Veiculo veiculoExistente = veiculoRepository.findById(veiculo.getCodigo());
        if (veiculoExistente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Veículo não encontrado.")
                    .build();
        }

        veiculoExistente.setModelo(veiculo.getModelo());
        veiculoExistente.setObservacoes(veiculo.getObservacoes());

        veiculoRepository.persistOrUpdate(veiculoExistente);

        return Response.ok(veiculoExistente).build();
    }
}
