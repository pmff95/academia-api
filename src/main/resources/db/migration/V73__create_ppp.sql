CREATE TABLE ppp (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
    escola_id BIGINT NOT NULL REFERENCES escola(id) ON DELETE CASCADE,
    inicio_vigencia DATE NOT NULL,
    fim_vigencia DATE NOT NULL,
    data_aprovacao DATE,
    responsavel VARCHAR(255),
    conteudo TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    version INT NOT NULL DEFAULT 0,
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_ppp_uuid ON ppp(uuid);
CREATE INDEX idx_ppp_escola ON ppp(escola_id);
