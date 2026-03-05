package br.com.reobote.service;

import br.com.reobote.entity.Cliente;
import br.com.reobote.entity.Telefone;
import br.com.reobote.repository.ClienteRepository;
import br.com.reobote.repository.TelefoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TelefoneService {

    @Autowired
    private TelefoneRepository telefoneRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // Buscar todos os telefones
    public List<Telefone> findAll() {
        return telefoneRepository.findAll();
    }

    // Buscar telefone por ID
    public Telefone findById(Long id) {
        return telefoneRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Telefone não encontrado com ID: " + id));
    }

    // Buscar telefones por cliente
    public List<Telefone> findByClienteId(Long clienteId) {
        // Verifica se o cliente existe
        clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
        
        return telefoneRepository.findByClienteIdCliente(clienteId);
    }

    public Telefone criarTelefone(Telefone telefone) {
        if (telefone.getCliente() == null || telefone.getCliente().getIdCliente() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }
        
        Long clienteId = telefone.getCliente().getIdCliente();
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        telefone.setCliente(cliente);
        return telefoneRepository.save(telefone);
    }

    public Telefone adicionarTelefoneCliente(Long clienteId, Telefone telefone) {
        Cliente cliente = clienteRepository.findById(clienteId)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        telefone.setCliente(cliente);
        telefone.setIdTelefone(null);
        
        return telefoneRepository.save(telefone);
    }
}