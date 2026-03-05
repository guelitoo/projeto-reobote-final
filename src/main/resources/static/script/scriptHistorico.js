document.addEventListener("DOMContentLoaded", carregarHistorico);

async function carregarHistorico() {
    try {
        const resp = await fetch("http://localhost:8080/api/pedidos");

        if (resp.status === 204) {
            document.getElementById("listaHistorico").innerHTML = "<p>Nenhum pedido encontrado.</p>";
            return;
        }

        const pedidos = await resp.json();
        renderizarHistorico(pedidos);

    } catch (erro) {
        console.error("Erro ao carregar histórico:", erro);
        document.getElementById("listaHistorico").innerHTML = "<p>Erro ao carregar histórico.</p>";
    }
}

function renderizarHistorico(pedidos) {
    const container = document.getElementById("listaHistorico");
    container.innerHTML = "";

    pedidos.forEach(p => {
        const card = document.createElement("div");
        card.className = "card-pedido";

        const statusFormatado = p.status.replace("_", " ");

        card.innerHTML = `
            <div class="card-header">
                <span>Pedido #${p.id}</span>
                <span class="status ${p.status}">${statusFormatado}</span>
            </div>

            <div class="card-body">
                <p><strong>Serviço:</strong> ${p.servico}</p>
                <p><strong>Diâmetro:</strong> ${p.diametro} mm</p>
                <p><strong>Comprimento:</strong> ${p.comprimento} mm</p>
                <p><strong>Quantidade:</strong> ${p.quantidade}</p>
                <p><strong>Tipo de borracha:</strong> ${p.tipoBorracha || "—"}</p>
                <p><strong>Preço Final:</strong> R$ ${p.precoFinal?.toFixed(2) || "—"}</p>
                
                <p class="excluido-info">
                    <strong>Excluído pelo funcionário:</strong> 
                    <span style="color:${p.excluido ? "red" : "green"};">
                        ${p.excluido ? "SIM" : "NÃO"}
                    </span>
                </p>
            </div>
        `;

        container.appendChild(card);
    });
}
