CREATE TABLE template_contrato_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    uuid UUID,
    nome VARCHAR(255),
    storage_key VARCHAR(255),
    placeholders TEXT,
    status VARCHAR(20),
    version INTEGER,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_template_contrato_aud_rev FOREIGN KEY (rev)
        REFERENCES revinfo(id) ON DELETE CASCADE
);
