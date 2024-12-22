package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lombok.extern.slf4j.Slf4j;
import io.smallrye.mutiny.Uni;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
@Slf4j
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, String> {

    public Uni<List<Veiculo>> obterModelosProcessados() {
        return Uni.createFrom().item(findAll().list());
    }

    @Transactional
    public void persistOrUpdate(Veiculo veiculo) {

        Veiculo existingVeiculo = find("codigo", veiculo.getCodigo()).firstResult();

        if (existingVeiculo != null) {

            existingVeiculo.setMarca(veiculo.getMarca() != null ? veiculo.getMarca() : existingVeiculo.getMarca());
            existingVeiculo.setModelo(veiculo.getModelo() != null ? veiculo.getModelo() : existingVeiculo.getModelo());
            existingVeiculo.setObservacoes(veiculo.getObservacoes() != null ? veiculo.getObservacoes() : existingVeiculo.getObservacoes());

            log.info("Atualizando veículo com código {}", veiculo.getCodigo());


            existingVeiculo.persist();
        } else {

            log.info("Criando novo veículo com código {}", veiculo.getCodigo());
            persist(veiculo);
        }
    }

    public List<Veiculo> findByMarca(String marca) {
        return find("LOWER(marca) = LOWER(?1)", marca).list();
    }

    public Veiculo findByCodigo(String codigo) {
        return find("codigo", codigo).firstResult();
    }
}
