package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import com.fipeapi1.services.FipeClient;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    @RestClient
    FipeClient fipeClient;  // Cliente para comunicação com a API FIPE

    @Inject
    VeiculoRepository veiculoRepository;  // Repositório para persistência de veículos

    /**
     * Canal de entrada para processar as marcas recebidas da fila.
     * Deserializa a mensagem JSON e salva os veículos.
     */
    @Incoming("marcas-da-api1")  // Consumindo o canal "marcas-da-api1"
    public void processarMarca(String mensagem) {
        // Converte a string JSON para JSONArray
        JSONArray jsonArray = new JSONArray(mensagem);

        List<Map<String, String>> marcas = new ArrayList<>();

        // Itera sobre o JSONArray e converte para List<Map<String, String>>
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // Converte o JSONObject para um Map<String, String> - fazendo a conversão dos valores para String
            Map<String, String> marca = (Map<String, String>) (Map<?, ?>) jsonObject.toMap();
            marcas.add(marca);
        }

        if (marcas.isEmpty()) {
            System.out.println("Nenhuma marca recebida.");
            return;
        }

        // Processa cada marca
        for (Map<String, String> marca : marcas) {
            String codigoMarca = marca.get("codigo");

            // Buscar os modelos dessa marca utilizando a API FIPE
            Map<String, Object> modelosResponse = fipeClient.obterModelos(codigoMarca);

            if (modelosResponse != null && modelosResponse.containsKey("modelos")) {
                List<Map<String, String>> modelos = (List<Map<String, String>>) modelosResponse.get("modelos");

                // Processar cada modelo encontrado
                for (Map<String, String> modelo : modelos) {
                    Veiculo veiculo = new Veiculo();
                    veiculo.setMarca(marca.get("nome"));  // Nome da marca
                    veiculo.setCodigo(modelo.get("codigo"));  // Código do modelo
                    veiculo.setModelo(modelo.get("nome"));  // Nome do modelo

                    // Salva o veículo no banco de dados
                    veiculoRepository.persist(veiculo);
                }

                // Agora que os modelos foram processados, você pode produzir os modelos para o canal.
                enviarModelosParaFila();  // Chamando o método sem parâmetros agora
            } else {
                System.out.println("Nenhum modelo encontrado para a marca: " + marca.get("nome"));
            }
        }
    }

    @Transactional
    @Outgoing("modelos-da-api2-out")
    public Multi<String> enviarModelosParaFila() {
        // Obtém a lista de modelos processados
        List<Veiculo> modelos = obterModelosProcessados();

        // Cria um JSONArray e popula com os modelos
        JSONArray jsonModelos = new JSONArray();

        for (Veiculo veiculo : modelos) {
            // Converte cada Veiculo para JSONObject
            JSONObject jsonVeiculo = new JSONObject();
            jsonVeiculo.put("id", veiculo.getId());
            jsonVeiculo.put("marca", veiculo.getMarca());
            jsonVeiculo.put("modelo", veiculo.getModelo());
            // Adicione outros campos conforme necessário

            jsonModelos.put(jsonVeiculo);
        }

        // Retorna um Multi com a string JSON dos modelos (fluxo reativo)
        return Multi.createFrom().item(jsonModelos.toString());
    }

    // Método auxiliar para obter os modelos processados
    private List<Veiculo> obterModelosProcessados() {
        return veiculoRepository.listAll();  // Aqui você pode transformar para o formato necessário
    }
}
