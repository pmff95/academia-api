-- Remove da tabela serie
ALTER TABLE serie_aud DROP COLUMN nivel_ensino;

-- Adiciona na tabela turma
ALTER TABLE turma_aud ADD COLUMN nivel_ensino VARCHAR(50);

