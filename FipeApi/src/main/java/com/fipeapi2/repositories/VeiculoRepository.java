package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, Long> {

    public Uni<List<Veiculo>> obterModelosProcessados() {
        return Uni.createFrom().item(listAll());
    }

    public void persistOrUpdate(Veiculo veiculo) {
        if (veiculo.getId() == null) {
            persist(veiculo);  // Persistir novo veículo
        } else {
            Veiculo existingVeiculo = findById(veiculo.getId());
            if (existingVeiculo != null) {
                // Atualiza os campos que foram alterados
                existingVeiculo.setMarca(veiculo.getMarca());
                existingVeiculo.setModelo(veiculo.getModelo());
                existingVeiculo.setCodigo(veiculo.getCodigo());
                persistOrUpdate(existingVeiculo); // Chama persistOrUpdate para atualizar
            } else {
                // Caso o veículo não exista, então deve ser persistido como novo
                persist(veiculo);
            }
        }
    }
}
