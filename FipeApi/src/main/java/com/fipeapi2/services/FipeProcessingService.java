package com.fipeapi2.services;

import com.exceptions.JsonProcessingExceptionCustom;
import com.fipeapi1.services.FipeClient;
import com.fipeapi1.services.ModelosResponse;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
@Slf4j
public class FipeProcessingService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    ObjectMapper objectMapper;

    @Transactional
    @Incoming("marcas-da-api1")
    public void processarVeiculos(String marcasJson) {
        try {

            List<Map<String, Object>> marcasList = objectMapper.readValue(marcasJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

            for (Map<String, Object> marcaObj : marcasList) {
                processarMarca(marcaObj);
            }

        } catch (JsonProcessingExceptionCustom e) {
            log.error("Erro ao processar o JSON da mensagem: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao processar a mensagem do Kafka: {}", e.getMessage());
        }
    }

    private void processarMarca(Map<String, Object> marca) {
        String codigoMarca = (String) marca.get("codigo");
        String nomeMarca = (String) marca.get("nome");
        String observacoes = (String) marca.getOrDefault("observacoes", "Nenhuma observação");

        log.info("Marca recebida - Nome: {} | Código: {}", nomeMarca, codigoMarca);

        try {

            ModelosResponse modelosResponse = fipeClient.obterModelos(codigoMarca);

            List<ModelosResponse.Modelo> modelos = modelosResponse.getModelos();

            final String modeloMarca = Optional.ofNullable(modelos)
                    .filter(list -> !list.isEmpty())
                    .map(list -> list.get(0).getNome())
                    .orElse("Desconhecido");

            veiculoRepository.find("codigo", codigoMarca)
                    .firstResultOptional()
                    .ifPresentOrElse(
                            veiculoExistente -> atualizarVeiculo(veiculoExistente, nomeMarca, modeloMarca, observacoes),
                            () -> criarVeiculo(codigoMarca, nomeMarca, modeloMarca, observacoes)
                    );
        } catch (Exception e) {
            log.error("Erro ao obter modelos para a marca {}: {}", nomeMarca, e.getMessage());
        }
    }

    private void atualizarVeiculo(Veiculo veiculoExistente, String nomeMarca, String modeloMarca, String observacoes) {
        veiculoExistente.setMarca(nomeMarca);
        veiculoExistente.setModelo(modeloMarca);
        veiculoExistente.setObservacoes(observacoes);
        veiculoExistente.persist();
        log.info("Veículo atualizado: {} - {}", nomeMarca, modeloMarca);
    }

    private void criarVeiculo(String codigoMarca, String nomeMarca, String modeloMarca, String observacoes) {
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(nomeMarca);
        novoVeiculo.setCodigo(codigoMarca);
        novoVeiculo.setModelo(modeloMarca);
        novoVeiculo.setObservacoes(observacoes);
        novoVeiculo.persist();
        log.info("Novo veículo criado: {} - {}", nomeMarca, modeloMarca);
    }
}
