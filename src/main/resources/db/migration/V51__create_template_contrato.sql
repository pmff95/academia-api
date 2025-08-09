CREATE TABLE template_contrato (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
    nome VARCHAR(255) NOT NULL,
    storage_key VARCHAR(255) NOT NULL,
    placeholders TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    version INT NOT NULL DEFAULT 0,
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_template_contrato_uuid ON template_contrato(uuid);
