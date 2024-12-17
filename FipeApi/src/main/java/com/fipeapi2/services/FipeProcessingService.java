package com.fipeapi2.services;

import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class FipeProcessingService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Transactional
    @Incoming("marcas-da-api1") // A origem do canal de mensagens
    public void processarVeiculos(String marcasJson) {
        JSONArray marcasArray = new JSONArray(marcasJson);

        for (int i = 0; i < marcasArray.length(); i++) {
            JSONObject marca = marcasArray.getJSONObject(i);
            String codigoMarca = marca.getString("codigo");
            String nomeMarca = marca.getString("nome");

            System.out.println("Processando marca: " + nomeMarca + " - Código: " + codigoMarca);

            // Criar o objeto Veiculo com as informações básicas
            Veiculo veiculo = new Veiculo();
            veiculo.setMarca(nomeMarca);
            veiculo.setCodigo(codigoMarca);

            // Salvar o Veículo no banco
            veiculoRepository.persistOrUpdate(veiculo);

            // Forçar o flush para garantir que os dados sejam persistidos
            PanacheEntityBase.flush();

            // Preencher os campos restantes (por exemplo, modelos)
            preencherModelos(codigoMarca, veiculo);
        }
    }

    // Exemplo de preenchimento de modelos
    private void preencherModelos(String codigoMarca, Veiculo veiculo) {
        // Aqui você poderia usar o fipeClient para fazer buscas por modelos, mas por enquanto vamos deixar um modelo fictício
        // Isso é só um exemplo, o ideal seria fazer a chamada ao FipeClient para buscar os modelos.
        String modelo = "Modelo Exemplo";  // Você substituiria isso por dados reais obtidos de outra API ou fonte de dados
        veiculo.setModelo(modelo);

        // Atualizando a base de dados
        veiculoRepository.persistOrUpdate(veiculo);

        // Forçar o flush novamente
        PanacheEntityBase.flush();
    }
}
