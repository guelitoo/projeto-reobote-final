package br.com.reobote.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.reobote.entity.Pedido;
import br.com.reobote.repository.PedidoRepository;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository produtoRepository;

    public List<Pedido> findAll() {
        return produtoRepository.findAll();
    }
    
    public void deletarPedido(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new RuntimeException("Pedido não encontrado");
        }
        produtoRepository.deleteById(id);
    }

	
    public Pedido calcularOrcamento(Pedido pedido) {
        double precoFinal = 0.0;

        switch (pedido.getServico().toLowerCase()) {
            case "usinagem" -> precoFinal = calcularUsinagem(pedido);
            case "retificacao" -> precoFinal = calcularRetificacao(pedido);
            case "revestimento" -> precoFinal = calcularRevestimento(pedido);
        }

        pedido.setPrecoFinal(precoFinal);
        return pedido;
    }

    private double calcularUsinagem(Pedido produto) {
        double precoPorCm2 = 0.05 * 20;
        double diametroCm = produto.getDiametro() / 10.0;
        double comprimentoCm = produto.getComprimento() / 10.0;
        double area = Math.PI * diametroCm * comprimentoCm;
        return area * precoPorCm2;
    }

    private double calcularRetificacao(Pedido pedido) {
        double precoPorCm = 0.5 * 6;
        double comprimentoCm = pedido.getComprimento() / 10.0;
        return comprimentoCm * precoPorCm;
    }

    private double calcularRevestimento(Pedido pedido) {
        double diametroCm = pedido.getDiametro() / 10.0;
        double comprimentoCm = pedido.getComprimento() / 10.0;

        double precoPorCm3 = switch (pedido.getTipoBorracha() != null ? pedido.getTipoBorracha().toLowerCase() : "natural") {
            case "nitrilica" -> 0.17;
            case "silicone" -> 0.20;
            case "neoprene" -> 0.18;
            default -> 0.15;
        };

        double raio = diametroCm / 2.0;
        double volume = Math.PI * Math.pow(raio, 2) * comprimentoCm;
        return volume * precoPorCm3;
    }

	public Optional<Pedido> findById(Long id) {
		return null;
	}

	public void save(Pedido pedido) {
		
	}
}
