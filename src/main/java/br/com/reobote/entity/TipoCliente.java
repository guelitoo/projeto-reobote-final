package br.com.reobote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_cliente")
public class TipoCliente {

    //Atributos
    @Id
    @Column(name = "id_tipo_cliente", length = 36)
    private Long idTipoCliente;

    @Column(name = "fisico")
    private String fisico;

    @Column(name = "empresa")
    private String empresa;

    //Relacionamentos
    @OneToOne
    @JoinColumn(name = "FK_CLIENTE_id_cliente")
    private Cliente cliente;

    //Construtor vazio
    public TipoCliente() {
    }

    //Construtor completo
    public TipoCliente(Long idTipoCliente, String fisico, String empresa, Cliente cliente) {
        this.idTipoCliente = idTipoCliente;
        this.fisico = fisico;
        this.empresa = empresa;
        this.cliente = cliente;
    }

	public Long getIdTipoCliente() {
		return idTipoCliente;
	}

	public void setIdTipoCliente(Long idTipoCliente) {
		this.idTipoCliente = idTipoCliente;
	}

	public String getFisico() {
		return fisico;
	}

	public void setFisico(String fisico) {
		this.fisico = fisico;
	}

	public String getEmpresa() {
		return empresa;
	}

	public void setEmpresa(String empresa) {
		this.empresa = empresa;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
