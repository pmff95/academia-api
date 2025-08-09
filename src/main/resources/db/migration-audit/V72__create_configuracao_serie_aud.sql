CREATE TABLE configuracao_serie_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    escola_id BIGINT,
    serie VARCHAR(50),
    tipo_periodo VARCHAR(20),
    quantidade_avaliacoes INTEGER,
    media NUMERIC(5,2),
    version INTEGER,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_configuracao_serie_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);
