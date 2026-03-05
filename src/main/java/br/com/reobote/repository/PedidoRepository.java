package br.com.reobote.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.reobote.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
	
	List<Pedido> findByClienteIdCliente(Long idCliente);
	
	List<Pedido> findByExcluidoFalseOrderByIdDesc();
	List<Pedido> findByClienteIdClienteOrderByIdDesc(Long idCliente);
	List<Pedido> findByClienteIdClienteAndExcluidoFalse(Long idCliente);
	
	



}