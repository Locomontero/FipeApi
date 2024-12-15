package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, Long> {

    public void persistOrUpdate(Veiculo veiculo) {
        if (veiculo.getId() == null) {
            persist(veiculo);
        } else {
            Veiculo existingVeiculo = findById(veiculo.getId());
            if (existingVeiculo != null) {
                existingVeiculo.setMarca(veiculo.getMarca());
                existingVeiculo.setModelo(veiculo.getModelo());
                existingVeiculo.setCodigo(veiculo.getCodigo());

            }
        }
    }
}
