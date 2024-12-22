package com.fipeapi1.services;

import com.exceptions.JsonProcessingExceptionCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fipeapi2.entities.Veiculo;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@ApplicationScoped
@Slf4j
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Channel("marcas-da-api1-out")
    Emitter<String> emitter;

    @PostConstruct
    public void carregarVeiculos() {
        buscarVeiculos()
                .onItem().transformToUni(veiculos -> {
                    if (veiculos != null && !veiculos.isEmpty()) {
                        log.info("Número de veículos recebidos da API externa: {}", veiculos.size());
                        processarVeiculos(veiculos);
                    } else {
                        log.info("Nenhum veículo recebido da API externa.");
                    }
                    return Uni.createFrom().voidItem();
                })
                .onFailure().invoke(e -> log.error("Erro ao carregar os veículos: {}", e.getMessage()))
                .subscribe().with(item -> {}, failure -> {});
    }

    private void processarVeiculos(List<Veiculo> veiculos) {
        try {
            String veiculosJson = objectMapper.writeValueAsString(veiculos);
            veiculosJson = veiculosJson.startsWith("[") ? veiculosJson : "[" + veiculosJson + "]";
            enviarParaKafka(veiculosJson);
        } catch (Exception e) {
            log.error("Erro ao serializar os veículos para JSON: {}", e.getMessage());
            throw new JsonProcessingExceptionCustom("Erro ao serializar os veículos para JSON", e);
        }
    }

    public void enviarParaKafka(String veiculosJson) {
        emitter.send(veiculosJson)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("Erro ao enviar veículos para o Kafka: {}", throwable.getMessage());
                    } else {
                        log.info("Enviado para Kafka: {}", veiculosJson);
                    }
                });
    }

    public Uni<List<Veiculo>> buscarVeiculos() {
        return Uni.createFrom().item(() -> {
            String jsonResponse = fipeClient.obterMarcas();
            return parseVeiculosJson(jsonResponse);
        });
    }

    private List<Veiculo> parseVeiculosJson(String jsonResponse) {
        try {
            return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, Veiculo.class));
        } catch (Exception e) {
            log.error("Erro ao processar o JSON: {}", e.getMessage());
            throw new JsonProcessingExceptionCustom("Erro ao processar o JSON", e);
        }
    }
}
