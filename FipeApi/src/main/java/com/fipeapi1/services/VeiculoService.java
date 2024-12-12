package com.fipeapi1.services;

import com.fipeapi2.entities.Veiculo;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VeiculoService {

    // Suponha que você tenha um repositório JPA ou algo similar para buscar os veículos
    public List<String> buscarMarcas() {
        // Lógica para buscar as marcas no banco de dados
        return List.of("Marca A", "Marca B", "Marca C");
    }

    // Lógica para buscar veículos por marca
    public List<String> buscarVeiculosPorMarca(String marca) {
        // Lógica para buscar os veículos da marca no banco
        return List.of("Veículo 1", "Veículo 2");
    }

    // Lógica para atualizar um veículo
    public void atualizarVeiculo(Long id, Veiculo veiculo) {
        // Lógica para atualizar o veículo no banco de dados
    }
}
