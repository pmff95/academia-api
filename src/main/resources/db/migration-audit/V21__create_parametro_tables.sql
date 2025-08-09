-- Tabela de auditoria de parametros globais
CREATE TABLE parametro_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    chave VARCHAR(100),
    descricao VARCHAR(255),
    valor_default VARCHAR(255),
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    version INT,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_parametro_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

-- Tabela de auditoria de parametros por escola
CREATE TABLE parametro_escola_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    escola_id BIGINT,
    parametro_id BIGINT,
    valor_customizado VARCHAR(255),
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    version INT,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_parametro_escola_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);


-- Adiciona coluna tipo na tabela de auditoria
ALTER TABLE parametro_aud
    ADD COLUMN IF NOT EXISTS tipo VARCHAR(20);
