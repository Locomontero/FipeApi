package com.fipeapi1.resources;

import com.fipeapi1.services.FipeService;
import com.fipeapi1.services.VeiculoService;
import com.fipeapi2.DTOs.VeiculoDTO;
import com.fipeapi2.entities.Veiculo;
import org.flywaydb.core.Flyway;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/fipe")
public class FipeResource {

    @Inject
    FipeService fipeService;

    @Inject
    VeiculoService veiculoService;

    @Inject
    Flyway flyway;

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
        List<Veiculo> veiculos = veiculoService.getTodosVeiculos();
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
            List<Veiculo> veiculos = veiculoService.getVeiculoByMarca(marca);

            List<VeiculoDTO> veiculoDTOs = veiculos.stream()
                    .map(veiculo -> new VeiculoDTO(
                            veiculo.getMarca(),
                            veiculo.getModelo(),
                            veiculo.getCodigo(),
                            veiculo.getObservacoes()))
                    .collect(Collectors.toList());

            return Response.ok(veiculoDTOs).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Nenhum veículo encontrado para a marca " + marca)
                    .build();
        }
    }

    @POST
    @Path("/alterar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response alterarVeiculo(@Valid VeiculoDTO veiculoDTO) {
        Veiculo veiculoAlterado = veiculoService.alterarVeiculo(veiculoDTO);
        if (veiculoAlterado == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Veículo não encontrado.")
                    .build();
        }
        return Response.ok(veiculoAlterado).build();
    }

    @GET
    @Path("/veiculo/{codigo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVeiculoByCodigo(@PathParam("codigo") String codigo) {
        // Agora utilizando o VeiculoService corretamente
        Veiculo veiculo = veiculoService.findByCodigo(codigo)
                .orElseThrow(() -> new NotFoundException("Veículo com código " + codigo + " não encontrado."));
        return Response.ok(veiculo).build();
    }

    @GET
    @Path("/migrate")
    public String applyMigrations() {
        try {
            flyway.migrate();
            return "Migrações aplicadas com sucesso!";
        } catch (Exception e) {
            return "Erro ao aplicar migrações: " + e.getMessage();
        }
    }
}
