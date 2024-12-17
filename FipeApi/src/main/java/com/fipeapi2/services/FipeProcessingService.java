package com.fipeapi2.services;

import com.fipeapi1.services.FipeClient;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    VeiculoRepository veiculoRepository;  // Repositório para persistir os dados dos veículos

    @Inject
    @Channel("modelos-da-api2-out")  // Canal de saída para enviar para o Kafka
    Emitter<String> emitter;

    @Inject
    @RestClient
    FipeClient fipeClient;  // Cliente para consumir a API FIPE

    @Transactional
    @Incoming("marcas-da-api1")  // Consumindo marcas da fila 'marcas-da-api1'
    public void processarVeiculos(String marcasJson) {
        JSONArray marcasArray = new JSONArray(marcasJson);

        for (int i = 0; i < marcasArray.length(); i++) {
            JSONObject marca = marcasArray.getJSONObject(i);
            String codigoMarca = marca.getString("codigo");
            String nomeMarca = marca.getString("nome");

            Veiculo veiculo = new Veiculo();
            veiculo.setMarca(nomeMarca);
            veiculo.setCodigo(codigoMarca);
            veiculoRepository.persist(veiculo);

            List<Map<String, String>> modelos = buscarModelos(codigoMarca);

            for (Map<String, String> modelo : modelos) {

                List<Map<String, String>> anos = buscarAnos(codigoMarca, modelo.get("codigo"));


                for (Map<String, String> ano : anos) {
                    Veiculo veiculoAno = new Veiculo();
                    veiculoAno.setMarca(nomeMarca);
                    veiculoAno.setCodigo(codigoMarca);
                    veiculoAno.setModelo(modelo.get("nome"));
                    veiculoAno.setAno(ano.get("codigo"));  // O código do ano
                    veiculoRepository.persist(veiculoAno);  // Persiste o ano no banco
                }
            }
        }
    }

    // Método para buscar os modelos com base no código da marca
    public List<Map<String, String>> buscarModelos(String codigoMarca) {
        return (List<Map<String, String>>) fipeClient.obterModelos(codigoMarca);  // Chama a API FIPE para obter os modelos
    }

    // Método para buscar os anos com base no código da marca e do modelo
    public List<Map<String, String>> buscarAnos(String codigoMarca, String codigoModelo) {
        return fipeClient.obterAnos(codigoMarca, codigoModelo);  // Chama a API FIPE para obter os anos
    }

    public Uni<List<Veiculo>> obterModelosProcessados() {
        return (Uni<List<Veiculo>>) veiculoRepository.listAll();  // Retorna a lista de veículos persistidos
    }


    public void enviarVeiculosParaKafka(String marcasJson) {
        emitter.send(marcasJson);
    }
}
