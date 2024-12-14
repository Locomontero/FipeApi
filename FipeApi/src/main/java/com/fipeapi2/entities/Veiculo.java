package com.fipeapi2.entities;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Veiculo extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Marca não pode ser nula")
    @Size(min = 2, max = 100, message = "A marca deve ter entre 2 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String marca;

    @NotNull(message = "Modelo não pode ser nulo")
    @Size(min = 1, max = 100, message = "O modelo deve ter entre 1 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String modelo;

    @NotNull(message = "Código não pode ser nulo")
    @Size(min = 1, max = 50, message = "O código deve ter entre 1 e 50 caracteres")
    @Column(nullable = false, length = 50, unique = true)
    private String codigo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
