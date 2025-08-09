CREATE TABLE controle_matricula (
                                    ano              INT PRIMARY KEY,
                                    letra_atual      VARCHAR(1)       NOT NULL,
                                    numero_atual     INT           NOT NULL,

                                    escola_id        BIGINT           NOT NULL,
                                    CONSTRAINT fk_controle_matricula_escola
                                        FOREIGN KEY (escola_id)
                                            REFERENCES escola(id),

                                    uuid             UUID          NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
                                    criado_em        TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    atualizado_em    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP
);
