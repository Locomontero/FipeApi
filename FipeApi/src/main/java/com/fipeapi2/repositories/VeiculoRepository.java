package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, Long> {

    // Não precisa implementar nada manualmente, Panache cuida disso.

    public void salvarVeiculo(Veiculo veiculo) {
        persist(veiculo);  // método do Panache
    }

    public List<Veiculo> listarVeiculos() {
        return listAll();  // método do Panache
    }

    public Veiculo buscarPorId(Long id) {
        return findById(id);  // método do Panache
    }

    public void removerVeiculo(Long id) {
        deleteById(id);  // método do Panache
    }
}
