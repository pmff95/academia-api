CREATE TABLE aula_horario_grade (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    dia VARCHAR(20) CHECK (dia IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA')) NOT NULL,
    inicio TIME NOT NULL,
    fim TIME NOT NULL,
    turma_id BIGINT NOT NULL,
    disciplina_id BIGINT NOT NULL,
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW(),
    status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_aula_horario_grade_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    CONSTRAINT fk_aula_horario_grade_disciplina FOREIGN KEY (disciplina_id) REFERENCES disciplina(id) ON DELETE CASCADE
);

CREATE INDEX idx_aula_horario_grade_turma ON aula_horario_grade(turma_id);
