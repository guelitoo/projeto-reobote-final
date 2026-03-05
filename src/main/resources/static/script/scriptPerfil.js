document.addEventListener('DOMContentLoaded', () => {
  // ==============================
  // 🔹 1. Verifica sessão
  // ==============================
  const usuarioJson = localStorage.getItem('usuarioLogado');

  if (!usuarioJson) {
    // Se não houver usuário logado, redireciona para o login
    window.location.href = '../pages/login.html';
    return;
  }

  const usuario = JSON.parse(usuarioJson);

  // ==============================
  // 🔹 2. Exibe dados do usuário
  // ==============================
  document.getElementById('nome').value = usuario.nome || '';
  document.getElementById('email').value = usuario.email || '';
  document.getElementById('cpfCnpj').value = usuario.cpfCnpj || '';
  document.getElementById('senha').value = usuario.senha || '';
  document.getElementById('telefone').value = usuario.telefones?.[0]?.numero || '';

  // ==============================
  // 🔹 2.1 Exibir botão "Meus Pedidos" para CLIENTE
  // ==============================
  const btnMeusPedidos = document.getElementById('btnMeusPedidos');

  if (usuario.tipoUsuario === 'cliente') {
    btnMeusPedidos.style.display = 'block';

    btnMeusPedidos.addEventListener('click', () => {
      window.location.href = './cliente.html'; 
      // Ajuste o caminho se necessário
    });
  }


  // ==============================
  // 🔹 3. Habilitar edição de campos ao clicar no ícone ✎
  // ==============================
  document.querySelectorAll('.edit-icon').forEach(icon => {
    icon.addEventListener('click', () => {
      const field = icon.dataset.edit;
      const input = document.getElementById(field);
      if (input) input.removeAttribute('readonly');
    });
  });

  // ==============================
  // 🔹 4. Alternar visibilidade da senha
  // ==============================
  const senhaInput = document.getElementById('senha');
  const toggleSenha = document.getElementById('toggleSenha');

  toggleSenha.addEventListener('click', () => {
    const isSenha = senhaInput.type === 'password';
    senhaInput.type = isSenha ? 'text' : 'password';
    toggleSenha.textContent = isSenha ? 'Ocultar senha' : 'Exibir senha';
  });

  // ==============================
  // 🔹 5. Logout
  // ==============================
  const logoutBtn = document.getElementById('logoutBtn');
  logoutBtn.addEventListener('click', () => {
    localStorage.removeItem('usuarioLogado');
    window.location.href = '../index.html';
  });

  // ==============================
  // 🔹 6. Atualizar dados do perfil
  // ==============================
  const form = document.getElementById('usuarioLogado');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();

    const dadosAtualizados = {
      idCliente: usuario.idCliente,
      nome: document.getElementById('nome').value,
      email: document.getElementById('email').value,
      cpfCnpj: document.getElementById('cpfCnpj').value,
      senha: document.getElementById('senha').value,
      telefones: [{ numero: document.getElementById('telefone').value }]
    };

    try {
      const response = await fetch(`http://localhost:8080/clientes/${usuario.idCliente}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(dadosAtualizados)
      });

      if (!response.ok) throw new Error('Falha ao salvar alterações');

      const usuarioAtualizado = await response.json();
      localStorage.setItem('usuarioLogado', JSON.stringify(usuarioAtualizado));

      alert('Alterações salvas com sucesso!');
      document.querySelectorAll('.profile-form input').forEach(input => input.setAttribute('readonly', true));

    } catch (error) {
      console.error(error);
      alert('Erro ao salvar alterações. Tente novamente.');
    }
  });

  // ==============================
  // 🔹 7. Corrige link da logo
  // ==============================
  const logoLink = document.querySelector('.logo-section a');
  if (logoLink) {
    logoLink.addEventListener('click', (e) => {
      e.preventDefault();

      const tipo = usuario.tipoUsuario;

      if (tipo === 'admin' || tipo === 'funcionario') {
        window.location.href = '../index.html';
      } else if (tipo === 'cliente') {
        window.location.href = '../index.html';
      } else {
        window.location.href = '../index.html';
      }
    });
  }
});
