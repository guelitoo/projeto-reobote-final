package br.com.reobote.repository;

import br.com.reobote.entity.Telefone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelefoneRepository extends JpaRepository<Telefone, Long> {
    
    // Buscar telefones por ID do cliente
    @Query("SELECT t FROM Telefone t WHERE t.cliente.idCliente = :clienteId")
    List<Telefone> findByClienteIdCliente(@Param("clienteId") Long clienteId);
    
    // Ou usando derived query (alternativa)
    // List<Telefone> findByClienteIdCliente(Long idCliente);
}