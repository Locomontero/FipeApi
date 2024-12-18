package com.fipeapi1.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fipeapi2.entities.Veiculo;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Channel;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.annotation.PostConstruct;
import java.util.List;

@ApplicationScoped
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;  // Cliente que faz a comunicação com a API externa

    @Inject
    @Channel("marcas-da-api1-out")
    Emitter<String> emitter;  // Emitter que envia dados para o Kafka

    private ObjectMapper objectMapper = new ObjectMapper(); // Para mapear objetos para JSON

    @PostConstruct
    public void carregarVeiculos() {
        try {
            // Chama o método para buscar os veículos da API 1
            List<Veiculo> veiculos = buscarVeiculos();

            if (veiculos != null && !veiculos.isEmpty()) {
                System.out.println("Número de veículos recebidos da API externa: " + veiculos.size());
                for (Veiculo veiculo : veiculos) {
                    System.out.println("Veículo - Código: " + veiculo.getCodigo() + ", Nome: " + veiculo.getMarca());
                }

                // Converte a lista de veículos para JSON antes de enviar para o Kafka
                String veiculosJson = objectMapper.writeValueAsString(veiculos);

                // Caso a String não seja um array JSON, converta ela para um array
                if (!veiculosJson.startsWith("[")) {
                    veiculosJson = "[" + veiculosJson + "]";
                }

                // Envia a String para o Kafka
                enviarVeiculosParaKafka(veiculosJson);
            } else {
                System.out.println("Nenhum veículo recebido da API externa.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar os veículos: " + e.getMessage());
        }
    }

    // Método para buscar os veículos via FipeClient
    public List<Veiculo> buscarVeiculos() {
        String jsonResponse = fipeClient.obterMarcas();  // Aqui retornamos a string JSON da API
        try {
            // Deserializa o JSON em uma lista de Veículos
            return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, Veiculo.class));
        } catch (Exception e) {
            System.out.println("Erro ao processar o JSON: " + e.getMessage());
            return null;
        }
    }

    private void enviarVeiculosParaKafka(String veiculosJson) {
        try {
            emitter.send(veiculosJson);  // Envia o JSON para o Kafka
            System.out.println("Enviando para Kafka: " + veiculosJson);
        } catch (Exception e) {
            System.out.println("Erro ao enviar veículos para o Kafka: " + e.getMessage());
        }
    }
}
