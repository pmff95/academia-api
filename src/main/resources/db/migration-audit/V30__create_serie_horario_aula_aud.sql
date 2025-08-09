CREATE TABLE serie_horario_aula_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    uuid UUID,
    serie VARCHAR(20),
    ordem INTEGER,
    inicio TIME,
    fim TIME,
    status VARCHAR(20),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_serie_horario_aula_aud_rev FOREIGN KEY (rev)
        REFERENCES revinfo(id) ON DELETE CASCADE
);
