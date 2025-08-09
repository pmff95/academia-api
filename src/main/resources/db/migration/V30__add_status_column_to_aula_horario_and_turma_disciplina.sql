-- Adicionando coluna 'status' na tabela 'aula_horario'
ALTER TABLE aula_horario
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ATIVO';

-- Adicionando coluna 'status' na tabela 'turma_disciplina'
ALTER TABLE turma_disciplina
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ATIVO';
