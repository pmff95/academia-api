-- Adicionando coluna 'nivel_ensino' na tabela 'serie'
ALTER TABLE serie_aud
    ADD COLUMN IF NOT EXISTS nivel_ensino VARCHAR(50);