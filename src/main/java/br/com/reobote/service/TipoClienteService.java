package br.com.reobote.service;

import br.com.reobote.entity.TipoCliente;
import br.com.reobote.repository.TipoClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TipoClienteService {

    private final TipoClienteRepository tipoClienteRepository;

    public TipoClienteService(TipoClienteRepository tipoClienteRepository) {
        this.tipoClienteRepository = tipoClienteRepository;
    }

    public List<TipoCliente> findAll() {
        return tipoClienteRepository.findAll();
    }

    public Optional<TipoCliente> findById(Long id) {
        return tipoClienteRepository.findById(id);
    }

    public TipoCliente save(TipoCliente tipoCliente) {
        return tipoClienteRepository.save(tipoCliente);
    }

    public TipoCliente update(Long id, TipoCliente tipoCliente) {
        if (tipoClienteRepository.existsById(id)) {
            tipoCliente.setIdTipoCliente(id);
            return tipoClienteRepository.save(tipoCliente);
        }
        return null;
    }

    public void delete(Long id) {
        tipoClienteRepository.deleteById(id);
    }
}
