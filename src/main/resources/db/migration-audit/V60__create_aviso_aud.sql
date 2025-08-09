CREATE TABLE aviso_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    uuid UUID,
    titulo VARCHAR(255),
    mensagem TEXT,
    usuario_id BIGINT,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INTEGER,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_aviso_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);
