package br.com.reobote.controllers;

import br.com.reobote.entity.Telefone;
import br.com.reobote.service.TelefoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/telefones")
@CrossOrigin(origins = "*")
public class TelefoneController {

    @Autowired
    private TelefoneService telefoneService;

    // GET - Buscar todos os telefones
    @GetMapping
    public ResponseEntity<List<Telefone>> getAllTelefones() {
        try {
            List<Telefone> telefones = telefoneService.findAll();
            return ResponseEntity.ok(telefones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // GET - Buscar telefone por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getTelefoneById(@PathVariable Long id) {
        try {
            Telefone telefone = telefoneService.findById(id);
            return ResponseEntity.ok(telefone);
        } catch (Exception e) {
            return ResponseEntity.status(404).body(
                Map.of("error", e.getMessage())
            );
        }
    }

    // GET - Buscar telefones por cliente
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<?> getTelefonesByCliente(@PathVariable Long clienteId) {
        try {
            List<Telefone> telefones = telefoneService.findByClienteId(clienteId);
            return ResponseEntity.ok(telefones);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of("error", e.getMessage())
            );
        }
    }

    // POST - Criar telefone (já existente)
    @PostMapping
    public ResponseEntity<?> criarTelefone(@RequestBody Telefone telefone) {
        try {
            System.out.println("=== CRIANDO TELEFONE ===");
            System.out.println("Número: " + telefone.getNumero());
            
            telefone.setIdTelefone(null);
            Telefone telefoneSalvo = telefoneService.criarTelefone(telefone);
            
            return ResponseEntity.ok(telefoneSalvo);
            
        } catch (Exception e) {
            System.err.println("=== ERRO AO CRIAR TELEFONE ===");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                Map.of("error", "Erro ao criar telefone: " + e.getMessage())
            );
        }
    }

    // POST - Adicionar telefone a cliente específico
    @PostMapping("/cliente/{clienteId}")
    public ResponseEntity<?> adicionarTelefoneCliente(
            @PathVariable Long clienteId, 
            @RequestBody Telefone telefone) {
        try {
            System.out.println("=== ADICIONANDO TELEFONE AO CLIENTE ===");
            System.out.println("Cliente ID: " + clienteId);
            System.out.println("Número: " + telefone.getNumero());
            
            Telefone telefoneSalvo = telefoneService.adicionarTelefoneCliente(clienteId, telefone);
            
            return ResponseEntity.ok(telefoneSalvo);
            
        } catch (Exception e) {
            System.err.println("=== ERRO AO ADICIONAR TELEFONE ===");
            e.printStackTrace();
            return ResponseEntity.badRequest().body(
                Map.of("error", "Erro ao adicionar telefone: " + e.getMessage())
            );
        }
    }
}