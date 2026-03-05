document.addEventListener('DOMContentLoaded', async () => {
  const pedidosContainer = document.getElementById('pedidosContainer');

  // === FUNÇÃO PARA CARREGAR PEDIDOS ===
  async function carregarPedidos() {
    try {
      const response = await fetch("http://localhost:8080/api/pedidos/funcionario")
	  
      if (!response.ok) throw new Error('Erro ao buscar pedidos');

      const pedidos = await response.json();
      pedidosContainer.innerHTML = '';

      pedidos.forEach(p => {
        const total = (p.quantidade && p.precoFinal) ? p.quantidade * p.precoFinal : 0;
        const tipoBorracha = p.servico?.toLowerCase() === 'revestimento' ? (p.tipoBorracha || '-') : '-';

        // Cria o card do pedido
        const card = document.createElement('div');
        card.classList.add('pedido-card');

        card.innerHTML = `
          <div class="pedido-header">
            <h2>Pedido #${p.id}</h2>
            ${p.status === "PRONTO" ? `<button class="btn-excluir" data-id="${p.id}">❌</button>` : ""}
          </div>

          <div class="pedido-info">
            <p><strong>Cliente:</strong> ${p.cliente?.nome || '-'}</p>
            <p><strong>Email:</strong> 
              ${p.cliente?.email ? `<a href="mailto:${p.cliente.email}">${p.cliente.email}</a>` : '-'}
            </p>
            <p><strong>Serviço:</strong> ${p.servico || '-'}</p>
            <p><strong>Borracha:</strong> ${tipoBorracha}</p>
            <p><strong>Medidas:</strong> ${p.diametro || '-'}mm x ${p.comprimento || '-'}mm</p>
            <p><strong>Quantidade:</strong> ${p.quantidade || 0}</p>
            <p><strong>Total:</strong> R$ ${total.toFixed(2)}</p>
          </div>

          ${
            p.id
              ? `<div class="pedido-imagem">
                  <a href="http://localhost:8080/api/pedidos/${p.id}/imagem" download="pedido_${p.id}.jpg" title="Clique para baixar a imagem">
                    <img src="http://localhost:8080/api/pedidos/${p.id}/imagem"
                         alt="Imagem do pedido #${p.id}"
                         onerror="this.style.display='none'" />
                  </a>
                </div>`
              : ''
          }

          <div class="pedido-acoes">
            <select data-id="${p.id}" class="status-select ${p.status.toLowerCase().replace('_', '-')}">
              <option value="A_FAZER" ${p.status === "A_FAZER" ? "selected" : ""}>A FAZER</option>
              <option value="EM_ANDAMENTO" ${p.status === "EM_ANDAMENTO" ? "selected" : ""}>EM ANDAMENTO</option>
              <option value="PRONTO" ${p.status === "PRONTO" ? "selected" : ""}>PRONTO</option>
            </select>
            <button class="btn-confirmar" data-id="${p.id}">CONFIRMAR</button>
          </div>
        `;

        pedidosContainer.appendChild(card);
      });

    } catch (error) {
      console.error('Erro ao carregar pedidos:', error);
      pedidosContainer.innerHTML = '<p>Aguardando pedidos.</p>';
    }
  }

  // === CHAMA A FUNÇÃO AO ABRIR A PÁGINA ===
  await carregarPedidos();

  // === ATUALIZAR STATUS E EXCLUIR PEDIDOS ===
  pedidosContainer.addEventListener('click', async (e) => {
    // Atualizar status
    if (e.target.classList.contains('btn-confirmar')) {
      const id = e.target.getAttribute('data-id');
      const select = document.querySelector(`select[data-id="${id}"]`);
      const novoStatus = select.value;

      try {
        const response = await fetch(`http://localhost:8080/api/pedidos/${id}/status`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ status: novoStatus }),
        });

        if (!response.ok) throw new Error('Erro ao atualizar status');

        alert(`✅ Status do pedido #${id} atualizado para "${novoStatus.replace('_', ' ')}"`);
        await carregarPedidos();

        document.querySelectorAll('.status-select').forEach(sel => {
          sel.className = `status-select ${sel.value.toLowerCase().replace('_', '-')}`;
        });
      } catch (error) {
        console.error(`Erro ao atualizar status do pedido ${id}:`, error);
        alert('❌ Não foi possível atualizar o status. Tente novamente.');
      }
    }

    // Excluir pedido
    if (e.target.classList.contains('btn-excluir')) {
      const id = e.target.getAttribute('data-id');
      if (confirm(`Deseja realmente excluir o pedido #${id}?`)) {
        try {
          const response = await fetch(`http://localhost:8080/api/pedidos/${id}`, {
            method: "DELETE",
          });

          if (!response.ok) throw new Error('Erro ao excluir pedido');

          alert(`🗑️ Pedido #${id} excluído com sucesso!`);
          await carregarPedidos();
        } catch (error) {
          console.error(`Erro ao excluir pedido ${id}:`, error);
          alert('❌ Não foi possível excluir o pedido. Tente novamente.');
        }
      }
    }
  });
});
