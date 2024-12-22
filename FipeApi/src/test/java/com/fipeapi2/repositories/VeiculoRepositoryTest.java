package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class VeiculoRepositoryTest {

    @Inject
    VeiculoRepository veiculoRepository;

    @Test
    @Transactional
    public void testPersistAndFindByCodigo() {

        Veiculo veiculo = new Veiculo();
        veiculo.setCodigo("1");
        veiculo.setMarca("Acura");
        veiculo.setModelo("Desconhecido");
        veiculo.setObservacoes("Informação ainda não disponível");

        veiculoRepository.persist(veiculo);

        Veiculo foundVeiculo = veiculoRepository.findByCodigo("1");

        assertNotNull(foundVeiculo, "Veículo não encontrado");

        assertEquals("Acura", foundVeiculo.getMarca(), "Marca não corresponde");
        assertEquals("Desconhecido", foundVeiculo.getModelo(), "Modelo não corresponde");
        assertEquals("Informação ainda não disponível", foundVeiculo.getObservacoes(), "Observações não correspondem");
    }

    @Test
    @Transactional
    public void testPersistOrUpdate() {

        Veiculo veiculo = new Veiculo();
        veiculo.setCodigo("2000");
        veiculo.setMarca("Fiat");
        veiculo.setModelo("Uno");
        veiculo.setObservacoes("Novo veículo");

        veiculoRepository.persistOrUpdate(veiculo);

        veiculo.setMarca("Fiat Updated");
        veiculo.setModelo("Uno Updated");
        veiculo.setObservacoes("Veículo atualizado");

        veiculoRepository.persistOrUpdate(veiculo);

        Veiculo updatedVeiculo = veiculoRepository.findByCodigo("2000");

        assertNotNull(updatedVeiculo, "Veículo não encontrado");

        assertEquals("Fiat Updated", updatedVeiculo.getMarca(), "Marca não foi atualizada");
        assertEquals("Uno Updated", updatedVeiculo.getModelo(), "Modelo não foi atualizado");
        assertEquals("Veículo atualizado", updatedVeiculo.getObservacoes(), "Observações não foram atualizadas");
    }


}
