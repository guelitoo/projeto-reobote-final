package br.com.reobote.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.reobote.entity.Cliente;
import br.com.reobote.entity.Telefone;
import br.com.reobote.service.ClienteService;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(origins = "*") // permite chamadas do front-end
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    // --- CRUD padrão ---
    @GetMapping
    public List<Cliente> getAll() {
        return clienteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getById(@PathVariable Long id) {
        return clienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Aqui é onde entra o PASSO 2
    // Ele garante que cada telefone receba o cliente antes de salvar.
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Cliente cliente) {
        try {
            if (cliente.getTelefones() != null) {
                for (Telefone tel : cliente.getTelefones()) {
                    tel.setCliente(cliente); // associa telefone -> cliente
                }
            }

            Cliente novoCliente = clienteService.save(cliente);
            return ResponseEntity.ok(novoCliente);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro ao cadastrar cliente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        Cliente updated = clienteService.update(id, cliente);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- LOGIN ---
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Cliente loginRequest) {
        Optional<Cliente> clienteOpt = clienteService.findByEmail(loginRequest.getEmail());

        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();

            if (cliente.getSenha().equals(loginRequest.getSenha())) {
                return ResponseEntity.ok(cliente);
            }
        }
        return ResponseEntity.status(401).body("Email ou senha inválidos");
    }
}
