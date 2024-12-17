package com.fipeapi1.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.services.FipeProcessingService;
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
    FipeClient fipeClient;

    @Inject
    @Channel("modelos-da-api2")
    Emitter<String> emitter;

    private ObjectMapper objectMapper = new ObjectMapper(); // Para mapear JSON para objetos

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
                enviarVeiculosParaKafka(veiculosJson);

            } else {
                System.out.println("Nenhum veículo recebido da API externa.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar os veículos: " + e.getMessage());
        }
    }

    // Método para buscar as marcas de veículos via FipeClient
    public List<Veiculo> buscarVeiculos() {
        String jsonResponse = fipeClient.obterMarcas();  // Aqui retornamos a string JSON
        try {
            // Deserializa o JSON em uma lista de Veículos
            return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, Veiculo.class));
        } catch (Exception e) {
            System.out.println("Erro ao processar o JSON: " + e.getMessage());
            return null;
        }
    }

    // Método para enviar a lista de veículos para o Kafka
    private void enviarVeiculosParaKafka(String veiculosJson) {
        try {
            emitter.send(veiculosJson);  // Envia o JSON para o Kafka
            System.out.println("Veículos enviados para o Kafka com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao enviar veículos para o Kafka: " + e.getMessage());
        }
    }
}
