package com.fipeapi2.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Column;

import javax.validation.constraints.Size;

@Entity
public class Veiculo extends PanacheEntityBase {


    @Size(min = 2, max = 100, message = "A marca deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    @JsonProperty("nome")
    private String marca;

    @Size(min = 1, max = 100, message = "O modelo deve ter entre 1 e 100 caracteres")
    @Column(length = 100)
    private String modelo;

    @Id
    @Size(min = 1, max = 50, message = "O código deve ter entre 1 e 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    @Column(nullable = true,length = 255)  //
    private String observacoes;


    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
