package com.fipeapi1.mappers;

import com.exceptions.JsonProcessingExceptionCustom;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.DTOs.VeiculoDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class FipeMapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String serializeVeiculosToJson(List<Veiculo> veiculos) {
        try {
            String veiculosJson = objectMapper.writeValueAsString(veiculos);
            return veiculosJson.startsWith("[") ? veiculosJson : "[" + veiculosJson + "]";
        } catch (Exception e) {
            throw new JsonProcessingExceptionCustom("Erro ao serializar os veículos para JSON", e);
        }
    }

    public Veiculo toEntity(VeiculoDTO dto) {
        Veiculo veiculo = new Veiculo();
        veiculo.setCodigo(dto.getCodigo());
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setObservacoes(dto.getObservacoes() != null ? dto.getObservacoes() : "Informação ainda não disponível");
        return veiculo;
    }

    public VeiculoDTO toDto(Veiculo veiculo) {

        String observacoes = veiculo.getObservacoes() != null ? veiculo.getObservacoes() : "Informação ainda não disponível";

        return new VeiculoDTO(
                veiculo.getMarca(),
                veiculo.getModelo(),
                veiculo.getCodigo(),
                observacoes
        );
    }
}
