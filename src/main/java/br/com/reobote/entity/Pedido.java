package br.com.reobote.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String servico;      
    private Double diametro;
    private Double comprimento;
    private Integer quantidade;
    private String tipoBorracha;     
    private Double precoFinal;
    private boolean excluido = false;
    
    
    
    
    @Lob
    @Column(name = "imagem", columnDefinition = "LONGBLOB")
    private byte[] imagem;


    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusProcesso status = StatusProcesso.A_FAZER;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;


    // Construtores
    public Pedido() {}

    public Pedido(String servico, Double diametro, Double comprimento, Integer quantidade, String tipoBorracha, boolean excluido) {
        this.servico = servico;
        this.diametro = diametro;
        this.comprimento = comprimento;
        this.quantidade = quantidade;
        this.tipoBorracha = tipoBorracha;
        this.excluido = excluido;
    }

    // Getters e Setters
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getServico() {
		return servico;
	}

	public void setServico(String servico) {
		this.servico = servico;
	}

	public Double getDiametro() {
		return diametro;
	}

	public void setDiametro(Double diametro) {
		this.diametro = diametro;
	}

	public Double getComprimento() {
		return comprimento;
	}

	public void setComprimento(Double comprimento) {
		this.comprimento = comprimento;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getTipoBorracha() {
		return tipoBorracha;
	}

	public void setTipoBorracha(String tipoBorracha) {
		this.tipoBorracha = tipoBorracha;
	}

	public Double getPrecoFinal() {
		return precoFinal;
	}

	public void setPrecoFinal(Double precoFinal) {
		this.precoFinal = precoFinal;
	}

    public StatusProcesso getStatus() { 
    	return status; 
    }
	
	public void setStatus(StatusProcesso status) {
	    this.status = status;
	}
	
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    public byte[] getImagem() {
        return imagem;
    }

    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }

	public void setDescricao(String descricao) {
		
	}
	public boolean isExcluido() {
	    return excluido;
	}

	public void setExcluido(boolean excluido) {
	    this.excluido = excluido;
	}

}
