package com.fipeapi1.services;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

@QuarkusTest
public class FipeServiceTest {

    @Mock
    FipeClient fipeClient;  // O cliente Fipe, que será mockado.

    @InjectMocks
    FipeService fipeService; // O serviço que será testado.

    @Test
    public void testBuscarVeiculos() {
        // Dado: uma resposta simulada do FipeClient.
        String jsonResponse = "[{\"codigo\": \"163\", \"marca\": \"Volkswagen\", \"modelo\": \"Golf\", \"observacoes\": \"Carro novo\"}]";

        // Simulando o comportamento do fipeClient para retornar a resposta simulada
        when(fipeClient.obterMarcas()).thenReturn(jsonResponse);

        // Quando: chamamos o método buscarVeiculos
        Uni<List<Veiculo>> uniVeiculos = fipeService.buscarVeiculos();

        // Então: verificamos se a resposta é a esperada.
        List<Veiculo> veiculos = uniVeiculos.await().indefinitely();

        // Verificando o conteúdo da lista
        assertNotNull(veiculos);
        assertEquals(1, veiculos.size());
        assertEquals("Volkswagen", veiculos.get(0).getMarca());
        assertEquals("Golf", veiculos.get(0).getModelo());
        assertEquals("163", veiculos.get(0).getCodigo());
    }

    @Test
    public void testBuscarVeiculosComErro() {
        // Simulando um erro no fipeClient
        when(fipeClient.obterMarcas()).thenThrow(new RuntimeException("Erro ao obter dados da API"));

        // Quando: chamamos o método buscarVeiculos
        Uni<List<Veiculo>> uniVeiculos = fipeService.buscarVeiculos();

        // Então: verificamos se ocorre um erro na execução
        RuntimeException exception = assertThrows(RuntimeException.class, () -> uniVeiculos.await().indefinitely());
        assertEquals("Erro ao obter dados da API", exception.getMessage());
    }
}
