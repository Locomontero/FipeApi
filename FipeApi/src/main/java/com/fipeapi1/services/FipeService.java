package com.fipeapi1.services;

import org.eclipse.microprofile.reactive.messaging.Outgoing;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
public class FipeService {

    private static final String FIPE_URL = "https://fipeapi.appspot.com/api/1/carros/marcas.json";

    @Outgoing("marcas-para-api2")
    public String carregarMarcas() {
        Client client = javax.ws.rs.client.ClientBuilder.newClient();
        WebTarget target = client.target(FIPE_URL);
        Response response = target.request().get();

        if (response.getStatus() == 200) {
            String marcasJson = response.readEntity(String.class);
            return marcasJson;  // O retorno será enviado para o Kafka
        } else {
            throw new RuntimeException("Erro ao acessar a API da FIPE: " + response.getStatus());
        }
    }
}
