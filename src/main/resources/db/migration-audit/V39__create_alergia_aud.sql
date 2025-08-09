CREATE TABLE alergia_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    uuid UUID,
    tipo VARCHAR(20),
    substancia VARCHAR(255),
    gravidade VARCHAR(20),
    observacoes TEXT,
    data_diagnostico DATE,
    cuidados_emergenciais BOOLEAN,
    aluno_id BIGINT,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INTEGER,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_alergia_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);
