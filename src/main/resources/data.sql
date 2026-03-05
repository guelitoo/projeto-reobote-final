INSERT IGNORE INTO cliente (id_cliente, cpf_cnpj, email, nome, senha) VALUES 
	(1, "000.000.000-00", "admin@admin.com", "Admin", "A2025"),
	(2, "454.599.188-20", "m36230989@gmail.com", "Miguel Silva Santos", "miguel"),
	(3, "555.666.777-89", "murilohenriqueribeirodasilva1@gmail.com", "Murilo Henrique Ribeiro da Silva", "murilo"),
	(4, "333.222.111-90", "mateussoliveiraa30@gmail.com", "Mateus Oliveira", "mateus");
	
INSERT IGNORE INTO telefone (id_telefone, numero, fk_cliente_id_cliente) VALUES 
	(1, "(15)12345-1234", 1),
	(2, "(11)94777-0244", 2),
	(3, "(15)98764-9834", 3),
	(4, "(15)92745-3348", 4);
	
INSERT IGNORE INTO endereco (id_endereco, bairro, cep, cidade, complemento, estado, numero, pais, rua, fk_cliente_id_cliente) VALUES
	(1, "Jardim das Azaléias", "18086-707", "Sorocaba", "", "São Paulo", "124", "Brasil" , "Rua João Beneditto Capitani", 2);
