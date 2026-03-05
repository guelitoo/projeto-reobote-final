// Função de máscara para telefone
function maskTelefone(value) {
    return value
        .replace(/\D/g, "") // Remove tudo que não é número
        .replace(/^(\d{2})(\d)/, "($1) $2") // Coloca parênteses nos dois primeiros dígitos
        .replace(/(\d{5})(\d)/, "$1-$2") // Adiciona hífen após os 5 primeiros dígitos
        .slice(0, 15); // Limita a quantidade de caracteres
}

// Função de máscara para CPF/CNPJ
function maskCpfCnpj(value) {
    value = value.replace(/\D/g, ""); // Remove tudo que não é número
    if (value.length <= 11) {
        value = value
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d)/, "$1.$2")
            .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
    } else {
        value = value
            .replace(/^(\d{2})(\d)/, "$1.$2")
            .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
            .replace(/\.(\d{3})(\d)/, ".$1/$2")
            .replace(/(\d{4})(\d)/, "$1-$2");
    }
    return value.slice(0, 18);
}

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("cadastroClienteForm");
    const telefoneInput = document.getElementById("telefone");
    const cpfInput = document.getElementById("cpf");

    telefoneInput.addEventListener("input", (e) => {
        e.target.value = maskTelefone(e.target.value);
    });

    cpfInput.addEventListener("input", (e) => {
        e.target.value = maskCpfCnpj(e.target.value);
    });

    form.addEventListener("submit", async (event) => {
        event.preventDefault();

        // Valores formatados com máscara
        const nome = document.getElementById("nome").value;
        const cpfCnpj = document.getElementById("cpf").value; // mantém a máscara
        const email = document.getElementById("email").value;
        const telefone = document.getElementById("telefone").value; // mantém a máscara
        const senha = document.getElementById("senha").value;

        try {
            const response = await fetch("http://localhost:8080/clientes", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    nome,
                    cpfCnpj,
                    email,
                    senha,
                    telefones: [
                        { numero: telefone }
                    ]
                })
            });

            const responseText = await response.text(); 

            if (!response.ok) {
                let errorMessage = 'Erro ao cadastrar cliente';
                try {
                    const errorData = JSON.parse(responseText);
                    errorMessage = errorData.message || errorMessage;
                } catch (e) {
                    console.error("Resposta do servidor (não JSON):", responseText);
                    errorMessage = responseText;
                }
                throw new Error(errorMessage);
            }

            const data = JSON.parse(responseText);
            localStorage.setItem('pessoaId', data.idCliente);

            window.location.href = './cadastroendereco.html';

        } catch (error) {
            console.error('Erro no cadastro:', error);
            alert(`Falha ao cadastrar cliente: ${error.message}`);
        }
    });
});
