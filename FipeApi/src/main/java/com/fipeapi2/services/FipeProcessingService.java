package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FipeProcessingService {

    public void processarMarcas(String marcasJson) {
        if (marcasJson == null || marcasJson.isEmpty()) {
            // Se o JSON de marcas estiver vazio ou nulo, lança uma exceção WebApplicationException com um código HTTP 400
            throw new WebApplicationException("Dados inválidos para processar marcas.", Response.Status.BAD_REQUEST);
        }

        // Lógica de processamento...

        // Exemplo de criação de um veículo
        Veiculo veiculo = new Veiculo();
        veiculo.setMarca("Marca Exemplo");
        veiculo.setModelo("Modelo Exemplo");
        veiculo.setCodigo("123456");

        // Aqui você pode adicionar a persistência ou outro tipo de lógica
    }
}
