CREATE TABLE serie_horario_itinerario (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
    turma_id BIGINT NOT NULL,
    dia VARCHAR(20) NOT NULL,
    ordem INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE UNIQUE INDEX uq_serie_horario_itinerario ON serie_horario_itinerario(turma_id, dia, ordem);
ALTER TABLE serie_horario_itinerario
    ADD CONSTRAINT fk_serie_horario_itinerario_turma
        FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE;
CREATE INDEX idx_serie_horario_itinerario_uuid ON serie_horario_itinerario(uuid);
