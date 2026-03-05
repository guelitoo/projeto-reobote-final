package br.com.reobote.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.reobote.entity.Pedido;
import br.com.reobote.entity.StatusProcesso;
import br.com.reobote.repository.ClienteRepository;
import br.com.reobote.repository.PedidoRepository;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // =========================
    // ADMIN – LISTA TODOS (INCLUSIVE EXCLUÍDOS)
    // =========================
    @GetMapping
    public ResponseEntity<List<Pedido>> listarTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    // =========================
    // FUNCIONÁRIO – LISTA APENAS NÃO EXCLUÍDOS
    // =========================
    @GetMapping("/funcionario")
    public ResponseEntity<List<Pedido>> listarPedidosFuncionario() {
        List<Pedido> pedidos = pedidoRepository.findByExcluidoFalseOrderByIdDesc();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    // =========================
    // CLIENTE – LISTA APENAS NÃO EXCLUÍDOS
    // =========================
    @GetMapping("/cliente/{idCliente}")
    public ResponseEntity<List<Pedido>> listarPedidosCliente(@PathVariable Long idCliente) {
        List<Pedido> pedidos = pedidoRepository.findByClienteIdClienteAndExcluidoFalse(idCliente);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    // =========================
    // HISTÓRICO POR CLIENTE (MOSTRA TODOS)
    // =========================
    @GetMapping("/cliente/{idCliente}/historico")
    public ResponseEntity<List<Pedido>> listarHistoricoCliente(@PathVariable Long idCliente) {
        List<Pedido> pedidos = pedidoRepository.findByClienteIdClienteOrderByIdDesc(idCliente);

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    // =========================
    // DOWNLOAD DA IMAGEM
    // =========================
    @GetMapping("/{id}/imagem/download")
    public ResponseEntity<byte[]> baixarImagem(@PathVariable Long id) {
        var pedido = pedidoRepository.findById(id).orElse(null);
        if (pedido == null || pedido.getImagem() == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=pedido_" + id + ".jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(pedido.getImagem());
    }

    // =========================
    // VISUALIZAR IMAGEM
    // =========================
    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> visualizarImagem(@PathVariable Long id) {
        var pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null || pedido.getImagem() == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(pedido.getImagem(), headers, HttpStatus.OK);
    }

    // =========================
    // CALCULAR ORÇAMENTO
    // =========================
    @PostMapping("/orcamento")
    public ResponseEntity<?> calcularOrcamento(@RequestBody Pedido pedido) {
        try {
            double precoBase = 10.0;
            double precoPorCm = 0.5;

            double precoFinal = precoBase + (pedido.getDiametro() * pedido.getComprimento() * precoPorCm);

            return ResponseEntity.ok(Map.of("precoFinal", precoFinal));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Erro ao calcular orçamento: " + e.getMessage()));
        }
    }

    // =========================
    // ATUALIZAR STATUS DO PEDIDO
    // =========================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {

        String novoStatusStr = body.get("status");

        StatusProcesso novoStatus;
        try {
            novoStatus = StatusProcesso.valueOf(novoStatusStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Status inválido");
        }

        var pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {
            return ResponseEntity.status(404).body("Pedido não encontrado");
        }

        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(Map.of("mensagem", "Status atualizado com sucesso!"));
    }

    // =========================
    // CRIAR PEDIDO COM IMAGEM
    // =========================
    @PostMapping(value = "/criar-com-imagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> criarPedidoComImagem(
            @RequestPart("pedido") String pedidoJson,
            @RequestPart(value = "imagem", required = false) MultipartFile imagem) {

        try {
            Pedido pedido = objectMapper.readValue(pedidoJson, Pedido.class);

            if (imagem != null && !imagem.isEmpty()) {
                pedido.setImagem(imagem.getBytes());
            }

            pedido.setStatus(StatusProcesso.A_FAZER);
            pedido.setExcluido(false);

            if (pedido.getCliente() != null && pedido.getCliente().getIdCliente() != null) {
                var cliente = clienteRepository.findById(pedido.getCliente().getIdCliente())
                        .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
                pedido.setCliente(cliente);
            }

            Pedido salvo = pedidoRepository.save(pedido);

            return ResponseEntity.ok(salvo);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

    // =========================
    // FUNCIONÁRIO – "EXCLUIR" (SOFT DELETE)
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluirPedido(@PathVariable Long id) {

        Pedido pedido = pedidoRepository.findById(id).orElse(null);

        if (pedido == null) {
            return ResponseEntity.notFound().build();
        }

        // apenas marca como excluído
        pedido.setExcluido(true);
        pedidoRepository.save(pedido);

        return ResponseEntity.ok(Map.of("mensagem", "Pedido movido para o histórico"));
    }
}
