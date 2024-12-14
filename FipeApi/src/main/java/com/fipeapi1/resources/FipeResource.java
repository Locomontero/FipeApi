package com.fipeapi1.resources;

import com.fipeapi1.services.FipeService;
import com.fipeapi2.entities.Veiculo; // Importando de API-2
import com.fipeapi2.repositories.VeiculoRepository; // Importando de API-2
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/fipe")
public class FipeResource {

    @Inject
    FipeService fipeService;  // Serviço para acessar e processar as marcas da FIPE

    @Inject
    VeiculoRepository veiculoRepository;  // Repositório da API-2 para interagir com a base de dados

    /**
     * Endpoint para acionar a carga inicial dos dados de veículos.
     * Este endpoint chama a API FIPE para buscar as marcas e envia para a fila de processamento.
     */
    @POST
    @Path("/carga-inicial")
    public Response cargaInicial() {
        // Aciona o processamento das marcas e envia para a fila
        fipeService.buscarMarcasEFila();
        return Response.status(Response.Status.ACCEPTED)  // Status 202 para processos assíncronos
                .entity("Carga inicial de marcas iniciada. Processamento em andamento.")
                .build();
    }

    /**
     * Endpoint para buscar as marcas armazenadas no banco de dados.
     * Este endpoint consulta todas as marcas salvas na base de dados da API-2.
     */
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

    /**
     * Endpoint para buscar os veículos de uma marca específica.
     * Este endpoint consulta os modelos e códigos de veículos de uma marca armazenada no banco de dados da API-2.
     */
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

    /**
     * Endpoint para salvar ou atualizar os dados de um veículo.
     * Este endpoint recebe os dados alterados de um veículo e salva no banco de dados da API-2.
     */
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
