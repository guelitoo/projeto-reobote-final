package br.com.reobote.service;

import br.com.reobote.entity.FormaEntrega;
import br.com.reobote.repository.FormaEntregaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FormaEntregaService {

    private final FormaEntregaRepository formaEntregaRepository;

    public FormaEntregaService(FormaEntregaRepository formaEntregaRepository) {
        this.formaEntregaRepository = formaEntregaRepository;
    }

    public List<FormaEntrega> findAll() {
        return formaEntregaRepository.findAll();
    }

    public Optional<FormaEntrega> findById(Long id) {
        return formaEntregaRepository.findById(id);
    }

    public FormaEntrega save(FormaEntrega formaEntrega) {
        return formaEntregaRepository.save(formaEntrega);
    }

    public FormaEntrega update(Long id, FormaEntrega formaEntrega) {
        if (formaEntregaRepository.existsById(id)) {
            formaEntrega.setIdFormaEntrega(id);
            return formaEntregaRepository.save(formaEntrega);
        }
        return null;
    }

    public void delete(Long id) {
        formaEntregaRepository.deleteById(id);
    }
}
