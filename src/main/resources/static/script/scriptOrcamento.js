document.addEventListener("DOMContentLoaded", () => {
  const servicoSelect = document.getElementById("servico");
  const tipoBorrachaContainer = document.getElementById("tipoBorrachaContainer");
  const tipoBorrachaSelect = document.getElementById("tipoBorracha");
  const diametroInput = document.getElementById("diametro");
  const comprimentoInput = document.getElementById("comprimento");
  const quantidadeInput = document.getElementById("quantidade");
  const valorElement = document.getElementById("valor");
  const confirmarBtn = document.querySelector(".btn-confirmar");
  const desenhoInput = document.getElementById("desenho");
  const fileName = document.getElementById("file-name");

  const API_URL_ORCAMENTO = "http://localhost:8080/api/pedidos/orcamento";
  const API_URL_CONFIRMAR = "http://localhost:8080/api/pedidos/criar-com-imagem";

  // Mostra o nome do arquivo selecionado
  desenhoInput.addEventListener("change", () => {
    fileName.textContent = desenhoInput.files.length > 0 ? desenhoInput.files[0].name : "";
  });

  const formatarMoeda = (valor) =>
    valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

  servicoSelect.addEventListener("change", () => {
    tipoBorrachaContainer.style.display =
      servicoSelect.value === "revestimento" ? "block" : "none";
    calcularOrcamento();
  });

  [servicoSelect, tipoBorrachaSelect, diametroInput, comprimentoInput, quantidadeInput].forEach(el => {
    el.addEventListener("change", calcularOrcamento);
  });

  // 🔹 Cálculo do orçamento
  async function calcularOrcamento() {
    const servico = servicoSelect.value;
    const tipoBorracha = tipoBorrachaSelect.value;
    const diametro = parseFloat(diametroInput.value);
    const comprimento = parseFloat(comprimentoInput.value);
    const quantidade = parseInt(quantidadeInput.value) || 1;

    if (!servico || isNaN(diametro) || isNaN(comprimento)) {
      valorElement.textContent = "R$ 0,00";
      return;
    }

    const produto = {
      servico,
      tipoBorracha: tipoBorracha || null,
      diametro,
      comprimento,
      quantidade
    };

    try {
      const response = await fetch(API_URL_ORCAMENTO, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(produto)
      });

      if (!response.ok) throw new Error("Erro ao calcular orçamento");

      const data = await response.json();
      const total = data.precoFinal * quantidade;
      valorElement.textContent = formatarMoeda(total);
    } catch (error) {
      console.error("Erro ao calcular orçamento:", error);
      valorElement.textContent = "Erro no cálculo";
    }
  }

  // 🔹 Enviar pedido completo com imagem
  confirmarBtn.addEventListener("click", async (e) => {
    e.preventDefault();

    const usuarioLogado = localStorage.getItem("usuarioLogado");
    if (!usuarioLogado) {
      alert("Usuário não logado!");
      return;
    }

    const cliente = JSON.parse(usuarioLogado);
    if (!cliente || !cliente.idCliente) {
      alert("ID do cliente não encontrado!");
      return;
    }

    const pedido = {
      servico: servicoSelect.value,
      tipoBorracha: tipoBorrachaSelect.value || null,
      diametro: parseFloat(diametroInput.value),
      comprimento: parseFloat(comprimentoInput.value),
      quantidade: parseInt(quantidadeInput.value),
      precoFinal: parseFloat(valorElement.textContent.replace(/[R$\s.]/g, "").replace(",", ".")) || 0,
      cliente: { idCliente: cliente.idCliente }
    };

    const formData = new FormData();
    formData.append("pedido", new Blob([JSON.stringify(pedido)], { type: "application/json" }));

    if (desenhoInput.files.length > 0) {
      formData.append("imagem", desenhoInput.files[0]);
    }

    try {
      const response = await fetch(API_URL_CONFIRMAR, {
        method: "POST",
        body: formData
      });

      if (!response.ok) {
        const errorData = await response.text();
        console.error("Erro na resposta:", errorData);
        throw new Error("Erro ao salvar pedido");
      }

      window.location.href = "confirmacaoPedido.html";
    } catch (error) {
      console.error("Erro ao confirmar pedido:", error);
      alert("❌ Erro ao confirmar pedido");
    }
  });

  calcularOrcamento();
});
