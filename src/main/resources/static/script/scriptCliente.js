document.addEventListener("DOMContentLoaded", () => {
    carregarMeusPedidos();
});

async function carregarMeusPedidos() {
    try {
        const userLogado = JSON.parse(localStorage.getItem("usuarioLogado"));

        if (!userLogado || !userLogado.idCliente) {
            console.error("Nenhum cliente logado!");
            return;
        }

        const resposta = await fetch(`http://localhost:8080/api/pedidos/cliente/${userLogado.idCliente}`);

        // 👉 Se não houver conteúdo, não tenta ler JSON
        if (resposta.status === 204) {
            console.warn("Nenhum pedido encontrado para esse cliente.");
            document.getElementById("listaPedidos").innerHTML = `
                <p class="sem-pedidos">Você ainda não fez nenhum pedido.</p>
            `;
            return;
        }

        if (!resposta.ok) {
            throw new Error("Erro ao buscar pedidos");
        }

        const pedidos = await resposta.json(); // Agora seguro

        renderizarPedidos(pedidos);

    } catch (erro) {
        console.error("Erro:", erro);
        document.getElementById("listaPedidos").innerHTML = `
            <p class="erro">Erro ao carregar seus pedidos.</p>
        `;
    }
}

function renderizarPedidos(pedidos) {
    const lista = document.getElementById("listaPedidos");
    lista.innerHTML = "";

    pedidos.forEach(pedido => {
        const item = document.createElement("div");
        item.classList.add("pedido-card");

        item.innerHTML = `
            <p><strong>ID:</strong> ${pedido.id}</p>
            <p><strong>Status:</strong> ${pedido.status}</p>
            <p><strong>Diametro:</strong> ${pedido.diametro} mm</p>
            <p><strong>Comprimento:</strong> ${pedido.comprimento} mm</p>
            <hr>
        `;

        lista.appendChild(item);
    });
}
