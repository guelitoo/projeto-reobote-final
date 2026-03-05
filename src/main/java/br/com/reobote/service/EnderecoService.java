package br.com.reobote.service;

import br.com.reobote.entity.Endereco;
import br.com.reobote.repository.EnderecoRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    public List<Endereco> findAll() {
        return enderecoRepository.findAll();
    }

    public Optional<Endereco> findById(Long id) {
        return enderecoRepository.findById(id);
    }

    public Endereco save(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    public Endereco update(Long id, Endereco endereco) {
        if (enderecoRepository.existsById(id)) {
            endereco.setIdEndereco(id);
            return enderecoRepository.save(endereco);
        }
        return null;
    }

    public void delete(Long id) {
        enderecoRepository.deleteById(id);
    }
}
