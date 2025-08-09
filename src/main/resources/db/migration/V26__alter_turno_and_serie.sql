-- Remove coluna 'turno' da tabela 'serie' se existir
ALTER TABLE serie
DROP COLUMN IF EXISTS turno;

-- Adiciona coluna 'turno' na tabela 'turma'
ALTER TABLE turma
    ADD COLUMN turno VARCHAR(20);
