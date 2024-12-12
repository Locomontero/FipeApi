package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@ApplicationScoped
public class VeiculoRepository {

    private final EntityManager em;

    public VeiculoRepository(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void salvarVeiculo(Veiculo veiculo) {
        em.persist(veiculo);
    }
}
