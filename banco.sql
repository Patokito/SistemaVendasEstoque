CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO categoria (nome) VALUES 
('Eletrônicos'),
('Cabos'),
('Iluminação'),
('Periféricos'),
('Hardware');

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL,
    codigo_barras VARCHAR(20),
    categoria_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    telefone VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE historico_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    produto_id INT,
    produto_nome VARCHAR(100),
    quantidade INT,
    preco_unitario DECIMAL(10,2),
    preco_total DECIMAL(10,2),
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE processar_cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT,
    data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2),

    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE processar_cliente_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    processar_cliente_id INT,
    produto_id INT,
    nome_produto VARCHAR(255),
    quantidade INT,
    preco_unitario DECIMAL(10,2),
    preco_total DECIMAL(10,2),

    FOREIGN KEY (processar_cliente_id) REFERENCES processar_cliente(id),
    FOREIGN KEY (produto_id) REFERENCES produto(id)
);

CREATE TABLE nivel (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nivel VARCHAR(10) NOT NULL
);

INSERT INTO nivel (nivel) VALUES 
('Admin'),
('Funcionario'),
('Estagiario');

CREATE TABLE funcionario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(200) NOT NULL,
    usuario VARCHAR(20) NOT NULL,
    senha VARCHAR(20) NOT NULL,
    nivel_id INT,
    FOREIGN KEY (nivel_id) REFERENCES nivel(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE processar_estoque (
    id INT AUTO_INCREMENT PRIMARY KEY,    
    produto_id INT NOT NULL,
    funcionario_id INT NOT NULL,
    quantidade INT NOT NULL,
    tipo_operacao ENUM('ENTRADA', 'SAIDA') NOT NULL,
    data_processamento DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (produto_id) REFERENCES produto(id),
    FOREIGN KEY (funcionario_id) REFERENCES funcionario(id)
);