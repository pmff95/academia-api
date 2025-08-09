-- TABELA ALUNO_TURMA
-- ==========================
CREATE TABLE aluno_turma (
    turma_id BIGINT NOT NULL,
    aluno_id BIGINT NOT NULL,
    PRIMARY KEY (turma_id, aluno_id),
    CONSTRAINT fk_aluno_turma_turma FOREIGN KEY (turma_id) REFERENCES turma(id) ON DELETE CASCADE,
    CONSTRAINT fk_aluno_turma_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE
);

CREATE INDEX idx_aluno_turma_turma ON aluno_turma(turma_id);
CREATE INDEX idx_aluno_turma_aluno ON aluno_turma(aluno_id);
