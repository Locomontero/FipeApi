package com.fipeapi2.repositories;

import com.fipeapi2.entities.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class VeiculoRepository implements PanacheRepositoryBase<Veiculo, String> {

    public Uni<List<Veiculo>> obterModelosProcessados() {
        return Uni.createFrom().item(listAll());
    }

    @Transactional
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

    public List<Veiculo> findByMarca(String marca) {
        TypedQuery<Veiculo> query = getEntityManager().createQuery(
                "SELECT v FROM Veiculo v WHERE LOWER(v.marca) = LOWER(:marca)", Veiculo.class
        );
        query.setParameter("marca", marca);
        return query.getResultList();
    }

    public Veiculo findByCodigo(String codigo) {
        TypedQuery<Veiculo> query = getEntityManager().createQuery("SELECT v FROM Veiculo v WHERE v.codigo = :codigo", Veiculo.class);
        query.setParameter("codigo", codigo);
        return query.getResultStream().findFirst().orElse(null);
    }

}
