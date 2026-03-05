document.addEventListener('DOMContentLoaded', () => {
  const form = document.getElementById('loginForm');

  form.addEventListener('submit', async (event) => {
    event.preventDefault();

    const email = document.getElementById('email').value.trim();
    const senha = document.getElementById('senha').value.trim();

    if (!email || !senha) {
      alert('Preencha todos os campos!');
      return;
    }

    try {
      const response = await fetch('http://localhost:8080/clientes/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, senha })
      });

      if (!response.ok) {
        if (response.status === 401) throw new Error('Email ou senha inválidos.');
        throw new Error('Erro na autenticação.');
      }

      const usuario = await response.json();

      // Define tipo de usuário com base no email ou papel retornado
      let tipoUsuario = "cliente";
      if (email === "admin@admin.com") tipoUsuario = "admin";
      else if (email.endsWith("@reobote.com")) tipoUsuario = "funcionario";

      // Armazena os dados do usuário logado
      localStorage.setItem("usuarioLogado", JSON.stringify({ ...usuario, tipoUsuario }));

      // Redireciona
      if (tipoUsuario === "admin" || tipoUsuario === "funcionario") {
        window.location.href = "../pages/funcionario.html";
      } else {
        window.location.href = "../index.html";
      }
    } catch (error) {
      alert(error.message);
      console.error('Erro no login:', error);
    }
  });
});
