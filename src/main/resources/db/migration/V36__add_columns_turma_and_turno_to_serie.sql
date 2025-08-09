-- Adicionar a coluna 'turma' na tabela 'serie'
ALTER TABLE serie
    ADD COLUMN IF NOT EXISTS turma VARCHAR(255);

-- Adicionar a coluna 'turno' na tabela 'serie'
ALTER TABLE serie
    ADD COLUMN IF NOT EXISTS turno VARCHAR(20);