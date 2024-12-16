package com.fipeapi2.services;

import com.fipeapi1.services.FipeClient;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import com.fipeapi1.services.FipeService;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    @RestClient
    FipeClient fipeClient;

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    FipeService fipeService;  // Serviço para obter dados da FIPE

    @Inject
    @Channel("modelos-da-api2-out")
    Emitter<String> emitter;  // O canal do Kafka para enviar os dados

    // Consumindo marcas do Kafka
    @Incoming("marcas-da-api1")
    public void processarMarca(Map<String, String> marca) {
        String codigoMarca = marca.get("codigo");
        String nomeMarca = marca.get("nome");

        // Busca os modelos da marca
        List<Map<String, String>> modelos = fipeService.buscarModelos(codigoMarca);

        // Para cada modelo, busca os anos e preços, e persiste no banco de dados
        for (Map<String, String> modelo : modelos) {
            String codigoModelo = modelo.get("codigo");
            List<Map<String, String>> anos = fipeService.buscarAnos(codigoMarca, codigoModelo);

            for (Map<String, String> ano : anos) {
                String codigoAno = ano.get("codigo");
                Map<String, Object> preco = fipeService.buscarPreco(codigoMarca, codigoModelo, codigoAno);

                // Criando e persistindo o veículo
                Veiculo veiculo = new Veiculo();
                veiculo.setMarca(nomeMarca);
                veiculo.setModelo(modelo.get("nome"));
                veiculo.setCodigo(codigoModelo);
                veiculo.setAno(ano.get("nome"));

                // Convertendo o preço de String para BigDecimal
                BigDecimal precoVeiculo = new BigDecimal(preco.get("preco").toString());
                veiculo.setPreco(precoVeiculo);  // Usando BigDecimal para o preço

                // Persistindo no banco de dados
                veiculoRepository.persist(veiculo);
            }
        }

        // Agora, enviar para o próximo fluxo no Kafka
        enviarModelosParaFila();  // Produzindo os dados para o Kafka
    }

    @Transactional
    public void enviarModelosParaFila() {
        List<Veiculo> veiculos = obterModelosProcessados();
        JSONArray jsonVeiculos = new JSONArray();

        // Convertendo os veículos em JSON
        for (Veiculo veiculo : veiculos) {
            JSONObject jsonVeiculo = new JSONObject();
            jsonVeiculo.put("id", veiculo.getId());
            jsonVeiculo.put("marca", veiculo.getMarca());
            jsonVeiculo.put("modelo", veiculo.getModelo());
            jsonVeiculo.put("ano", veiculo.getAno());
            jsonVeiculo.put("preco", veiculo.getPreco().toString());
            jsonVeiculo.put("observacoes", veiculo.getObservacoes());
            jsonVeiculos.put(jsonVeiculo);
        }

        emitter.send(jsonVeiculos.toString());
    }

    private List<Veiculo> obterModelosProcessados() {
        return veiculoRepository.listAll();  // Retorna todos os veículos persistidos
    }
}
