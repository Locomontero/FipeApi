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

            fipeService.buscarMarcas();
            return Response.status(Response.Status.ACCEPTED)  // Status 202 para processos assíncronos
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
    public Response getMarcas() {
        // Retorna todas as marcas armazenadas no banco de dados da API-2
        List<Veiculo> marcas = veiculoRepository.listAll();
        if (marcas.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Nenhuma marca encontrada.")
                    .build();
        }
        return Response.ok(marcas).build();
    }

    @GET
    @Path("/modelos/{marca}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getModelosPorMarca(@PathParam("marca") String marca) {
        // Retorna os modelos e códigos dos veículos de uma marca na API-2
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
        // Atualiza ou insere o veículo no banco de dados da API-2
        veiculoRepository.persistOrUpdate(veiculo);
        return Response.ok(veiculo).build();
    }
}
