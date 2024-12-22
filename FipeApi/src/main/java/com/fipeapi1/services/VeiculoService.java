package com.fipeapi1.services;

import com.fipeapi1.mappers.FipeMapper;
import com.fipeapi2.DTO.VeiculoDTO;
import com.fipeapi2.entities.Veiculo;
import com.fipeapi2.repositories.VeiculoRepository;
import com.exceptions.VeiculosNotFoundException;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Slf4j
@RequiredArgsConstructor
public class VeiculoService {

    @Inject
    VeiculoRepository veiculoRepository;

    @Inject
    FipeMapper fipeMapper;

    public List<Veiculo> getTodosVeiculos() {
        return veiculoRepository.listAll();
    }

    public Veiculo alterarVeiculo(@Valid VeiculoDTO veiculoDTO) {
        try {
            Veiculo veiculoAlterado = fipeMapper.toEntity(veiculoDTO);

            Veiculo veiculoExistente = veiculoRepository.findByIdOptional(veiculoDTO.getCodigo())
                    .orElseThrow(() -> new NotFoundException("Veículo com código " + veiculoDTO.getCodigo() + " não encontrado"));

            veiculoExistente.setMarca(veiculoAlterado.getMarca());
            veiculoExistente.setModelo(veiculoAlterado.getModelo());
            veiculoExistente.setObservacoes(veiculoAlterado.getObservacoes());

            veiculoRepository.persistOrUpdate(veiculoExistente);

            return veiculoExistente;
        } catch (Exception e) {
            log.error("Erro ao alterar veículo", e);
            throw new InternalServerErrorException("Erro ao alterar veículo", e);
        }
    }

    public List<Veiculo> getVeiculoByMarca(String marca) {
        List<Veiculo> veiculos = veiculoRepository.findByMarca(marca);

        if (veiculos.isEmpty()) {
            throw new VeiculosNotFoundException(marca);
        }

        return veiculos;
    }

    public Optional<Veiculo> findByCodigo(String codigo) {
        Veiculo veiculo = veiculoRepository.find("codigo", codigo).firstResult();
        return Optional.ofNullable(veiculo);
    }
}
