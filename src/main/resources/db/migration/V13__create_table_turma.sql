-- TABELA TURMA
-- ==========================

CREATE TABLE turma (
                       id BIGINT PRIMARY KEY,
                       uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
                       nome VARCHAR(255) NOT NULL,
                       serie_id BIGINT NOT NULL,
                       criado_em TIMESTAMP DEFAULT NOW(),
                       atualizado_em TIMESTAMP DEFAULT NOW(),
                       version INT NOT NULL DEFAULT 0,

                       CONSTRAINT fk_turma_serie_id
                           FOREIGN KEY (serie_id) REFERENCES serie(id)
                               ON DELETE NO ACTION
);

-- Índices para facilitar buscas por ano letivo e nome
CREATE INDEX idx_turma_nome ON turma(nome);
