package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, String> {

    public Uni<List<Veiculo>> obterModelosProcessados() {
        return Uni.createFrom().item(listAll());
    }

    public void persistOrUpdate(Veiculo veiculo) {

        Veiculo existingVeiculo = find("codigo", veiculo.getCodigo()).firstResult();

        if (existingVeiculo != null) {
            existingVeiculo.setMarca(veiculo.getMarca());
            existingVeiculo.setModelo(veiculo.getModelo());
            existingVeiculo.setObservacoes(veiculo.getObservacoes());


            existingVeiculo.persist();
        } else {

            persist(veiculo);
        }
    }

}
