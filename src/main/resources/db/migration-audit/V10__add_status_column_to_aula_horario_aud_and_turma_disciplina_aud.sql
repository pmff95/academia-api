-- Adicionando coluna 'status' na tabela 'aula_horario'
ALTER TABLE aula_horario_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);

-- Adicionando coluna 'status' na tabela 'turma_disciplina'
ALTER TABLE turma_disciplina_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
