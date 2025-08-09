-- Criação da tabela de medicamentos
CREATE TABLE medicamento (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    dosagem VARCHAR(255),
    frequencia VARCHAR(255),
    observacoes TEXT,
    aluno_id BIGINT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW(),
    version INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_medicamento_uuid ON medicamento(uuid);
CREATE INDEX idx_medicamento_aluno ON medicamento(aluno_id);
