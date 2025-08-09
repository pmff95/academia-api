-- TABELA ESCOLA_MODULO
-- ==========================
CREATE TABLE escola_modulo (
    id BIGSERIAL PRIMARY KEY,
    escola_id BIGINT NOT NULL REFERENCES escola(id) ON DELETE CASCADE,
    modulo VARCHAR(50) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT FALSE,
    data_ativacao DATE,
    data_expiracao DATE
);

CREATE INDEX idx_escola_modulo_escola ON escola_modulo(escola_id);
CREATE INDEX idx_escola_modulo_modulo ON escola_modulo(modulo);
