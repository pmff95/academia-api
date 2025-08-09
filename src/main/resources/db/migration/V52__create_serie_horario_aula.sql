CREATE TABLE serie_horario_aula (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
    serie VARCHAR(20) NOT NULL,
    ordem INTEGER NOT NULL,
    inicio TIME NOT NULL,
    fim TIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_serie_horario_aula_serie_ordem ON serie_horario_aula(serie, ordem);
CREATE INDEX idx_serie_horario_aula_uuid ON serie_horario_aula(uuid);
