package br.com.reobote.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/teste")
public class TesteController {

    @PostMapping("/cliente-simples")
    public ResponseEntity<String> testeClienteSimples(@RequestBody Map<String, String> dados) {
        try {
            System.out.println("Dados recebidos: " + dados);
            return ResponseEntity.ok("Cliente recebido com sucesso: " + dados.get("nome"));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erro: " + e.getMessage());
        }
    }
}
