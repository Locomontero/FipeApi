package com.fipeapi1.services;


import com.exceptions.JsonProcessingExceptionCustom;
import com.exceptions.VeiculosNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import javax.ws.rs.NotFoundException;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.List;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    @Channel("marcas-da-api1-out")
    Emitter<String> emitter;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void carregarVeiculos() {
        buscarVeiculos()
                .onItem().transformToUni(veiculos -> {
                    if (veiculos != null && !veiculos.isEmpty()) {
                        log.info("Número de veículos recebidos da API externa: {}", veiculos.size());
                        veiculos.forEach(veiculo ->
                                log.info("Veículo - Código: {}, Nome: {}", veiculo.getCodigo(), veiculo.getMarca())
                        );

                        try {
                            String veiculosJson = objectMapper.writeValueAsString(veiculos);
                            veiculosJson = veiculosJson.startsWith("[") ? veiculosJson : "[" + veiculosJson + "]";

                            enviarVeiculosParaKafka(veiculosJson);
                        } catch (Exception e) {
                            log.error("Erro ao serializar os veículos para JSON: {}", e.getMessage());
                            throw new JsonProcessingExceptionCustom("Erro ao serializar os veículos para JSON", e);
                        }
                    } else {
                        log.info("Nenhum veículo recebido da API externa.");
                    }
                    return Uni.createFrom().voidItem();
                })
                .onFailure().invoke(e -> log.error("Erro ao carregar os veículos: {}", e.getMessage()))
                .subscribe().with(item -> {}, failure -> {});
    }

    public Uni<List<Veiculo>> buscarVeiculos() {
        return Uni.createFrom().item(() -> {
            String jsonResponse = fipeClient.obterMarcas();
            try {
                return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, Veiculo.class));
            } catch (Exception e) {
                log.error("Erro ao processar o JSON: {}", e.getMessage());
                throw new JsonProcessingExceptionCustom("Erro ao processar o JSON", e);
            }
        });
    }

    private void enviarVeiculosParaKafka(String veiculosJson) {
        emitter.send(veiculosJson)
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.error("Erro ao enviar veículos para o Kafka: {}", throwable.getMessage());
                    } else {
                        log.info("Enviando para Kafka: {}", veiculosJson);
                    }
                });
    }

    public List<Veiculo> getTodosVeiculos() {
        return veiculoRepository.listAll();
    }

    public Veiculo alterarVeiculo(Veiculo veiculo) {
        Veiculo veiculoExistente = veiculoRepository.findById(veiculo.getCodigo());

        if (veiculoExistente == null) {
            return null;
        }

        veiculoExistente.setMarca(veiculo.getMarca());
        veiculoExistente.setModelo(veiculo.getModelo());
        veiculoExistente.setObservacoes(veiculo.getObservacoes());

        veiculoRepository.persistOrUpdate(veiculoExistente);

        return veiculoExistente;
    }

    public List<Veiculo> getVeiculoByMarca(String marca) {
        List<Veiculo> veiculos = veiculoRepository.findByMarca(marca);

        if (veiculos.isEmpty()) {
            throw new VeiculosNotFoundException(marca);
        }

        return veiculos;
    }

    public Veiculo getVeiculoByCodigo(String codigo) {
        Veiculo veiculo = veiculoRepository.findByCodigo(codigo);

        if (veiculo == null) {
            throw new NotFoundException("Veículo com o código " + codigo + " não encontrado.");
        }

        return veiculo;
    }
}
