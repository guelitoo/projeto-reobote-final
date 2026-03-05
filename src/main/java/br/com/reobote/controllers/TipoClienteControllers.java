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

import br.com.reobote.entity.TipoCliente;
import br.com.reobote.service.TipoClienteService;

@RestController
@RequestMapping("/tipoclientes")
public class TipoClienteControllers {

    private TipoClienteService tipoClienteService;

    public TipoClienteControllers(TipoClienteService tipoClienteService) {
        this.tipoClienteService = tipoClienteService;
    }

    @GetMapping
    public List<TipoCliente> getAll() {
        return tipoClienteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCliente> getById(@PathVariable Long id) {
        return tipoClienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public TipoCliente create(@RequestBody TipoCliente tipoCliente) {
        return tipoClienteService.save(tipoCliente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCliente> update(@PathVariable Long id, @RequestBody TipoCliente tipoCliente) {
        TipoCliente updated = tipoClienteService.update(id, tipoCliente);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tipoClienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
