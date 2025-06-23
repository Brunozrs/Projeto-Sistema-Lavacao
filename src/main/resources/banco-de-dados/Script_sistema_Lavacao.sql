CREATE DATABASE IF NOT EXISTS db_lavacao_2025;
USE db_lavacao_2025;

CREATE TABLE cor(
                    id INT NOT NULL auto_increment,
                    nome varchar(50) NOT NULL,
                    CONSTRAINT pk_cor
                        PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE marca(
                      id INT NOT NULL auto_increment,
                      nome  varchar(50) NOT NULL,
                      CONSTRAINT pk_marca
                          PRIMARY KEY(id)
) engine=InnoDB;

CREATE TABLE modelo(
                       id INT NOT NULL auto_increment,
                       descricao varchar(200),
                       id_marca INT NOT NULL ,
                       eCategoria ENUM ('PEQUENO','MEDIO','GRANDE','MOTO','PADRAO') NOT NULL DEFAULT 'PADRAO',
                       CONSTRAINT pk_modelo
                           PRIMARY KEY(id),
                       CONSTRAINT fk_modelo_marca
                           FOREIGN KEY(id_marca)
                               REFERENCES marca(id)
) engine=InnoDB;

/*TABELA MOTOR COM RELACIONAMENTO 1:1 PARA MODELO*/
CREATE TABLE motor(
                      id_modelo INT NOT NULL REFERENCES modelo(id),
                      potencia INT NOT NULL DEFAULT 0,
                      tipoCombustivel ENUM('GASOLINA', 'ETANOL', 'FLEX','DIESEL','GNV','OUTRO') NOT NULL DEFAULT 'GASOLINA',
                      CONSTRAINT pk_motor PRIMARY KEY (id_modelo),
                      CONSTRAINT fk_motor_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id) ON DELETE CASCADE
) engine=InnoDB;

CREATE TABLE veiculo(
                        id INT NOT NULL AUTO_INCREMENT,
                        placa VARCHAR (50),
                        observacoes VARCHAR(200),
                        id_cor INT NOT NULL,
                        id_modelo INT NOT NULL,
                        CONSTRAINT pk_veiculo PRIMARY KEY (id),
                        CONSTRAINT fk_veiculocor FOREIGN KEY(id_cor) REFERENCES cor(id),
                        CONSTRAINT fk_veiculo_modelo FOREIGN KEY (id_modelo) REFERENCES modelo(id)
)engine=InnoDB;


INSERT INTO cor(nome) VALUES('Vermelho');
INSERT INTO cor(nome) VALUES('Verde');
INSERT INTO cor(nome) VALUES('Azul');

INSERT INTO marca(nome) VALUES('Fiat');
INSERT INTO marca(nome) VALUES('Lamborghini');
INSERT INTO marca(nome) VALUES('Corvette');
INSERT INTO marca(nome) VALUES('Ferrari');

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

INSERT INTO veiculo(placa,observacoes,id_cor,id_modelo) VALUES ('123-ABC','Carro',1,1);
INSERT INTO veiculo(placa,observacoes,id_cor,id_modelo) VALUES ('234-CDE','Carro',2,2);
INSERT INTO veiculo(placa,observacoes,id_cor,id_modelo) VALUES ('456-FGH','Carro',3,3);
INSERT INTO veiculo(placa,observacoes,id_cor,id_modelo) VALUES ('678-IJK','Carro',4,4);