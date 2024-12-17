package com.fipeapi1.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.services.FipeProcessingService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    FipeProcessingService fipeProcessingService;

    @PostConstruct
    public void carregarVeiculos() {
        try {

            List<Veiculo> veiculos = buscarVeiculos();

            if (veiculos != null && !veiculos.isEmpty()) {
                System.out.println("Número de veículos recebidos da API externa: " + veiculos.size()); // Log do tamanho da lista
                for (Veiculo veiculo : veiculos) {
                    System.out.println("Veículo - Código: " + veiculo.getCodigo() + ", Nome: " + veiculo.getMarca()); // Log de cada veículo
                }


                enviarVeiculosParaKafka(veiculos);


                fipeProcessingService.processarVeiculos(veiculos.toString());
            } else {
                System.out.println("Nenhum veículo recebido da API externa.");
            }

        } catch (Exception e) {
            System.out.println("Erro ao carregar os veículos: " + e.getMessage());
        }
    }

    public List<Veiculo> buscarVeiculos() {
        return fipeClient.obterMarcas();
    }

    private void enviarVeiculosParaKafka(List<Veiculo> veiculos) {
        try {

            JSONArray veiculosJson = new JSONArray();
            for (Veiculo veiculo : veiculos) {
                JSONObject veiculoJson = new JSONObject();
                veiculoJson.put("codigo", veiculo.getCodigo());
                veiculoJson.put("nome", veiculo.getMarca());
                veiculosJson.put(veiculoJson);
            }

            fipeProcessingService.enviarVeiculosParaKafka(veiculosJson.toString());
            System.out.println("Veículos enviados para o Kafka com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao enviar veículos para o Kafka: " + e.getMessage());
        }
    }

    public List<Map<String, String>> buscarModelos(String codigoVeiculo) {
        return fipeProcessingService.buscarModelos(codigoVeiculo);  // Chama o serviço da API 2 para buscar os modelos
    }

    public List<Map<String, String>> buscarAnos(String codigoVeiculo, String codigoModelo) {
        return fipeProcessingService.buscarAnos(codigoVeiculo, codigoModelo);  // Chama o serviço da API 2 para buscar os anos
    }
}
