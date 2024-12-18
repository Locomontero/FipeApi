package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Transactional
    @Incoming("marcas-da-api1")
    public void processarVeiculos(String marcasJson) {

        try {
            JSONArray marcasArray = new JSONArray(marcasJson);

            for (int i = 0; i < marcasArray.length(); i++) {
                JSONObject marca = marcasArray.getJSONObject(i);
                String codigoMarca = marca.optString("codigo");
                String nomeMarca = marca.optString("nome");
                String modeloMarca = marca.optString("modelo", "Desconhecido");

                System.out.println("Marca recebida - Nome: " + nomeMarca + " | Código: " + codigoMarca);

                // Verifica se o veículo já existe
                Veiculo veiculoExistente = Veiculo.find("codigo", codigoMarca).firstResult();

                if (veiculoExistente != null) {
                    // Se o veículo já existe, atualiza os dados
                    veiculoExistente.setMarca(nomeMarca);
                    veiculoExistente.setModelo(modeloMarca);  // Atualiza o modelo se necessário
                    veiculoExistente.setObservacoes(marca.optString("observacoes", null));

                    // Não precisa de persistOrUpdate aqui, a transação já garante isso
                    veiculoExistente.persist();  // Vai persistir a atualização na transação atual
                } else {
                    // Se o veículo não existe, cria um novo
                    Veiculo novoVeiculo = new Veiculo();
                    novoVeiculo.setMarca(nomeMarca);
                    novoVeiculo.setCodigo(codigoMarca);
                    novoVeiculo.setModelo(modeloMarca);
                    novoVeiculo.setObservacoes(marca.optString("observacoes", null));

                    // Persiste o novo veículo
                    novoVeiculo.persist();  // Vai persistir o novo veículo dentro da transação
                }
            }

        } catch (Exception e) {
            System.out.println("Erro ao processar a mensagem do Kafka: " + e.getMessage());
        }
    }
}
