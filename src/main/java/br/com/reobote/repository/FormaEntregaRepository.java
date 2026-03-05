package br.com.reobote.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.reobote.entity.FormaEntrega;

@Repository
public interface FormaEntregaRepository extends JpaRepository<FormaEntrega, Long> {
}
