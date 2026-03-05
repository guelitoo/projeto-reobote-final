package br.com.reobote.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.reobote.entity.FormaEntrega;
import br.com.reobote.service.FormaEntregaService;

@RestController
@RequestMapping("/formasentrega")
public class FormaEntregaController {

    private FormaEntregaService formaEntregaService;

    public FormaEntregaController(FormaEntregaService formaEntregaService) {
        this.formaEntregaService = formaEntregaService;
    }

    @GetMapping
    public List<FormaEntrega> getAll() {
        return formaEntregaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FormaEntrega> getById(@PathVariable Long id) {
        return formaEntregaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public FormaEntrega create(@RequestBody FormaEntrega formaEntrega) {
        return formaEntregaService.save(formaEntrega);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FormaEntrega> update(@PathVariable Long id, @RequestBody FormaEntrega formaEntrega) {
        FormaEntrega updated = formaEntregaService.update(id, formaEntrega);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        formaEntregaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
