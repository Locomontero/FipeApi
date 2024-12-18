package com.fipeapi1.services;

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
import java.util.List;

@ApplicationScoped
public class FipeService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    @Channel("marcas-da-api1-out")
    Emitter<String> emitter;  // Emitter que envia dados para o Kafka

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void carregarVeiculos() {
        try {

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

    public List<Veiculo> buscarVeiculos() {
        String jsonResponse = fipeClient.obterMarcas();
        try {

            return objectMapper.readValue(jsonResponse, objectMapper.getTypeFactory().constructCollectionType(List.class, Veiculo.class));
        } catch (Exception e) {
            System.out.println("Erro ao processar o JSON: " + e.getMessage());
            return null;
        }
    }

    private void enviarVeiculosParaKafka(String veiculosJson) {
        try {
            emitter.send(veiculosJson);
            System.out.println("Enviando para Kafka: " + veiculosJson);
        } catch (Exception e) {
            System.out.println("Erro ao enviar veículos para o Kafka: " + e.getMessage());
        }
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
            throw new NotFoundException("Veículos com a marca " + marca + " não encontrados.");
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
