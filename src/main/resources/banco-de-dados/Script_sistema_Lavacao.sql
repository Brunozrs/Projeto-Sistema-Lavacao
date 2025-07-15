CREATE DATABASE IF NOT EXISTS db_lavacao_2025;
USE db_lavacao_2025;

CREATE TABLE cor(
                    id INT NOT NULL auto_increment,
                    nome varchar(50) NOT NULL,
                    CONSTRAINT pk_cor PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE marca(
                      id INT NOT NULL auto_increment,
                      nome  varchar(50) NOT NULL,
                      CONSTRAINT pk_marca PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE servico(
                        id int NOT NULL auto_increment,
                        descricao varchar(150) NOT NULL,
                        valor decimal(10,2) NOT NULL,
                        pontos int NOT NULL DEFAULT 0,
                        categoria ENUM('PEQUENO', 'MEDIO', 'GRANDE', 'MOTO', 'PADRAO') NOT NULL DEFAULT 'PADRAO',
                        CONSTRAINT pk_servico PRIMARY KEY(id)
) engine=InnoDB;


CREATE TABLE modelo(
                       id INT NOT NULL auto_increment,
                       descricao varchar(200),
                       id_marca INT NOT NULL ,
                       eCategoria ENUM ('PEQUENO','MEDIO','GRANDE','MOTO','PADRAO') NOT NULL DEFAULT 'PADRAO',
                       CONSTRAINT pk_modelo PRIMARY KEY(id),
                       CONSTRAINT fk_modelo_marca FOREIGN KEY(id_marca) REFERENCES marca(id)
) engine=InnoDB;

/*TABELA MOTOR COM RELACIONAMENTO 1:1 PARA MODELO*/
CREATE TABLE motor(
                      id_modelo INT NOT NULL REFERENCES modelo(id),
                      potencia INT NOT NULL DEFAULT 0,
                      tipoCombustivel ENUM('GASOLINA', 'ETANOL', 'FLEX','DIESEL','GNV','OUTRO') NOT NULL DEFAULT 'GASOLINA',
                      CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
                      CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id)
                          ON DELETE CASCADE
) engine=InnoDB;

CREATE TABLE cliente(
                        id int NOT NULL auto_increment,
                        nome varchar(150) NOT NULL,
                        celular varchar(20) NOT NULL,
                        email varchar(150) NOT NULL,
                        data_cadastro date NOT NULL,
                        CONSTRAINT pk_cliente PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_fisica(
                                            id_cliente int NOT NULL,
                                            cpf varchar(150) NOT NULL,
    data_nascimento date NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY(id_cliente),
    CONSTRAINT fk_pf_cliente FOREIGN KEY(id_cliente) REFERENCES cliente(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS pessoa_juridica(
                                              id_cliente int NOT NULL,
                                              cnpj varchar(150) NOT NULL,
    inscricao_estadual varchar(150) NOT NULL,
    CONSTRAINT pk_cliente PRIMARY KEY(id_cliente),
    CONSTRAINT fk_pj_cliente FOREIGN KEY(id_cliente) REFERENCES cliente(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    ) engine=InnoDB;

CREATE TABLE IF NOT EXISTS veiculo(
                                      id int NOT NULL auto_increment,
                                      placa varchar(10) NOT NULL,
    observacoes varchar(350) NOT NULL DEFAULT 'Não informado',
    id_cliente int NOT NULL,
    id_modelo int NOT NULL,
    id_cor int NOT NULL,
    CONSTRAINT pk_veiculo PRIMARY KEY(id),
    CONSTRAINT fk_veiculo_cliente FOREIGN KEY(id_cliente) REFERENCES cliente(id)
    ON DELETE CASCADE,
    CONSTRAINT fk_veiculo_modelo FOREIGN KEY(id_modelo) REFERENCES modelo(id),
    CONSTRAINT fk_veiculo_cor FOREIGN KEY(id_cor) REFERENCES cor(id)
    ) engine=InnoDB;

CREATE TABLE ordem_de_servico(
                                 numero int NOT NULL auto_increment,
                                 total decimal(10,2) NOT NULL,
                                 agenda date NOT NULL,
                                 desconto double NOT NULL,
                                 situacao ENUM('ABERTA', 'FECHADA', 'CANCELADA') NOT NULL DEFAULT 'ABERTA',
                                 id_veiculo int NOT NULL,
                                 CONSTRAINT pk_os PRIMARY KEY (numero),
                                 CONSTRAINT fk_os_veiculo FOREIGN KEY(id_veiculo) REFERENCES veiculo(id)
) engine=InnoDB;


CREATE TABLE itemOS(
                       id int NOT NULL auto_increment,
                       valor_do_servico decimal(10,2) NOT NULL,
                       observacao VARCHAR(300) NOT NULL DEFAULT 'Não informado',
                       id_servico int NOT NULL,
                       id_os int NOT NULL,
                       CONSTRAINT pk_itemOS PRIMARY KEY(id),
                       CONSTRAINT fk_item_servico FOREIGN KEY(id_servico) REFERENCES servico(id),
                       CONSTRAINT fk_item_os FOREIGN KEY(id_os) REFERENCES ordem_de_servico(numero)
                           ON DELETE CASCADE
) engine=InnoDB;


INSERT INTO cor(nome) VALUES('Vermelho');
INSERT INTO cor(nome) VALUES('Verde');
INSERT INTO cor(nome) VALUES('Azul');
INSERT INTO cor(nome) VALUES('Amarelo');

INSERT INTO marca(nome) VALUES('Fiat');
INSERT INTO marca(nome) VALUES('Lamborghini');
INSERT INTO marca(nome) VALUES('Corvette');
INSERT INTO marca(nome) VALUES('Ferrari');

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação por Fora','50','10','PADRAO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa','70','15','PADRAO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Detalhada','90','20','PADRAO');

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação por Fora','43','10','PEQUENO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa','63','15','PEQUENO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Detalhada','83','20','PEQUENO');

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação por Fora','45','10','MEDIO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa','65','15','MEDIO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Detalhada','85','20','MEDIO');

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação por Fora','55','10','GRANDE');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa','75','15','GRANDE');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Detalhada','95','20','GRANDE');

INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação por Fora','40','10','MOTO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Completa','50','15','MOTO');
INSERT INTO servico(descricao, valor, pontos, categoria) VALUES('Lavação Detalhada','80','20','MOTO');

INSERT INTO modelo(descricao, id_marca,eCategoria) VALUES('Cronos', '1','PEQUENO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca,eCategoria) VALUES( 'Diablo', '2','MEDIO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca,eCategoria) VALUES('C7 Stingray', '3','PADRAO');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);
INSERT INTO modelo(descricao, id_marca,eCategoria) VALUES('12Clindri Spider', '4','GRANDE');
INSERT INTO motor(id_modelo) (SELECT max(id) FROM modelo);

UPDATE motor SET potencia = 200, tipoCombustivel ='DIESEL' WHERE id_modelo = 1;
UPDATE motor SET potencia=600, tipoCombustivel='GASOLINA' WHERE id_modelo = 2;
UPDATE motor SET potencia=1078, tipoCombustivel='FLEX' WHERE id_modelo = 3;
UPDATE motor SET potencia=1200, tipoCombustivel='GNV' WHERE id_modelo = 4;

INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Bruno', '(11) 91111-1111', 'bruno@gmail.com', '2025-05-01');
INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), '111.111.111-11', '1970-01-10');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Adriano', '(22) 92222-2222', 'adriano@gmail.com', '2024-11-02');
INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), '22.222.222/0002-22', '123456789');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Angelica', '(33) 93333-3333', 'angelica@gmail.com', '2024-11-03');
INSERT INTO pessoa_fisica(id_cliente, cpf, data_nascimento) VALUES((SELECT max(id) FROM cliente), '333.333.333-33', '1980-03-20');
INSERT INTO cliente(nome, celular,  email, data_cadastro) VALUES('Evandro', '(44) 94444-4444', 'evandro@gmail.com', '2024-11-02');
INSERT INTO pessoa_juridica(id_cliente, cnpj, inscricao_estadual) VALUES((SELECT max(id) FROM cliente), '44.444.444/0004-44', '987654321');

INSERT INTO veiculo(placa,observacoes,id_cliente,id_cor,id_modelo) VALUES ('123-ABC','Carro',1,1,1);
INSERT INTO veiculo(placa,observacoes,id_cliente,id_cor,id_modelo) VALUES ('234-CDE','Carro',2,2,2);
INSERT INTO veiculo(placa,observacoes,id_cliente,id_cor,id_modelo) VALUES ('456-FGH','Carro',3,3,3);
INSERT INTO veiculo(placa,observacoes,id_cliente,id_cor,id_modelo) VALUES ('678-IJK','Carro',4,4,4);

INSERT INTO ordem_de_servico(total, agenda, desconto, situacao, id_veiculo) VALUES('120', '2025-02-01', '0', 'ABERTA', '1');
INSERT INTO itemOS(valor_do_servico, observacao, id_servico, id_os) VALUES('60', 'Não informado', '1', '1');
INSERT INTO itemOS(valor_do_servico, observacao, id_servico, id_os) VALUES('60', 'Não informado', '1', '1');