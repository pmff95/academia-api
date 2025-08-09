-- Adicionar a coluna 'turma' na tabela 'serie'
ALTER TABLE serie_aud
    ADD COLUMN IF NOT EXISTS turma VARCHAR(255);

-- Adicionar a coluna 'turno' na tabela 'serie'
ALTER TABLE serie_aud
    ADD COLUMN IF NOT EXISTS turno VARCHAR(20);