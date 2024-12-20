package com.fipeapi2.services;

import com.exceptions.JsonProcessingExceptionCustom;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Map;

@ApplicationScoped
@Slf4j
public class FipeProcessingService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Transactional
    @Incoming("marcas-da-api1")
    public void processarVeiculos(String marcasJson) {
        try {
            JSONArray marcasArray = new JSONArray(marcasJson);

            // Alterado: Verificar se o objeto é um HashMap ou JSONObject antes de processar
            marcasArray.toList().forEach(marcaObj -> {
                if (marcaObj instanceof Map) {
                    processarMarca(new JSONObject((Map<?, ?>) marcaObj));
                } else {
                    log.error("Objeto inesperado recebido: {}", marcaObj.getClass().getName());
                }
            });

        } catch (JsonProcessingExceptionCustom e) {
            log.error("Erro ao processar o JSON da mensagem: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Erro inesperado ao processar a mensagem do Kafka: {}", e.getMessage());
        }
    }

    private void processarMarca(JSONObject marca) {
        String codigoMarca = marca.optString("codigo");
        String nomeMarca = marca.optString("nome");
        String modeloMarca = marca.optString("modelo", "Desconhecido");

        log.info("Marca recebida - Nome: {} | Código: {}", nomeMarca, codigoMarca);

        Veiculo veiculoExistente = veiculoRepository.find("codigo", codigoMarca).firstResult();
        if (veiculoExistente != null) {
            atualizarVeiculo(veiculoExistente, nomeMarca, modeloMarca, marca);
        } else {
            criarVeiculo(codigoMarca, nomeMarca, modeloMarca, marca);
        }
    }

    private void atualizarVeiculo(Veiculo veiculoExistente, String nomeMarca, String modeloMarca, JSONObject marca) {
        veiculoExistente.setMarca(nomeMarca);
        veiculoExistente.setModelo(modeloMarca);
        veiculoExistente.setObservacoes(marca.optString("observacoes", null));
        veiculoExistente.persist();
    }

    private void criarVeiculo(String codigoMarca, String nomeMarca, String modeloMarca, JSONObject marca) {
        Veiculo novoVeiculo = new Veiculo();
        novoVeiculo.setMarca(nomeMarca);
        novoVeiculo.setCodigo(codigoMarca);
        novoVeiculo.setModelo(modeloMarca);
        novoVeiculo.setObservacoes(marca.optString("observacoes", null));
        novoVeiculo.persist();
    }
}
