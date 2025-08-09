-- Adiciona a coluna nivel_ensino na tabela de auditoria disciplina_aud
ALTER TABLE disciplina_aud
    ADD COLUMN nivel_ensino VARCHAR(50);

-- Remove da tabela turma
ALTER TABLE turma_aud DROP COLUMN nivel_ensino;
