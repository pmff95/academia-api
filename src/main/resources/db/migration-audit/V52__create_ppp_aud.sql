CREATE TABLE ppp_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    uuid UUID,
    escola_id BIGINT,
    inicio_vigencia DATE,
    fim_vigencia DATE,
    data_aprovacao DATE,
    responsavel VARCHAR(255),
    conteudo TEXT,
    status VARCHAR(20),
    version INTEGER,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_ppp_aud_rev FOREIGN KEY (rev)
        REFERENCES revinfo(id) ON DELETE CASCADE
);
