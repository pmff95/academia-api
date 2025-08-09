-- Criação da tabela de alergias
CREATE TABLE alergia (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('ALIMENTAR','MEDICAMENTOSA','AMBIENTAL','OUTRA')),
    substancia VARCHAR(255) NOT NULL,
    gravidade VARCHAR(20) NOT NULL CHECK (gravidade IN ('LEVE','MODERADA','GRAVE')),
    observacoes TEXT,
    data_diagnostico DATE,
    cuidados_emergenciais BOOLEAN DEFAULT FALSE,
    aluno_id BIGINT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW(),
    version INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_alergia_uuid ON alergia(uuid);
CREATE INDEX idx_alergia_aluno ON alergia(aluno_id);
