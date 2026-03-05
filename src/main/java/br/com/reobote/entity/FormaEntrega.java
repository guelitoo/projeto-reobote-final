package br.com.reobote.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "forma_entrega")
public class FormaEntrega {

    @Id
    @Column(name = "id_forma_entrega", length = 36)
    private Long idFormaEntrega;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "valor")
    private Double valor;


    // Construtor vazio
    public FormaEntrega() {
    }

    // Construtor completo
    public FormaEntrega(Long idFormaEntrega, String descricao, Double valor) {
        this.idFormaEntrega = idFormaEntrega;
        this.descricao = descricao;
        this.valor = valor;
    }

    // Getters e Setters
    public Long getIdFormaEntrega() {
        return idFormaEntrega;
    }

    public void setIdFormaEntrega(Long idFormaEntrega) {
        this.idFormaEntrega = idFormaEntrega;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

}
