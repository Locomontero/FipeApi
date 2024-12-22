package com.fipeapi2.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo extends PanacheEntityBase {

    @Size(min = 2, max = 100, message = "A marca deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    @JsonProperty("nome")
    private String marca;

    @Size(min = 1, max = 100, message = "O modelo deve ter entre 1 e 100 caracteres")
    @Column(length = 100)
    @JsonProperty("modelo")
    private String modelo;

    @Id
    @Size(min = 1, max = 50, message = "O código deve ter entre 1 e 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    @JsonProperty("codigo")
    private String codigo;

    @Column(nullable = false, length = 255)
    @JsonProperty("observacoes")
    private String observacoes;
}
