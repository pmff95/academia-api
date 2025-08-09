CREATE TABLE medicamento_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    uuid UUID,
    nome VARCHAR(255),
    dosagem VARCHAR(255),
    frequencia VARCHAR(255),
    observacoes TEXT,
    aluno_id BIGINT,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INTEGER,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_medicamento_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);
