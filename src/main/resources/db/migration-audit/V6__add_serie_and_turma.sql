-- Removendo a coluna 'turno' da tabela 'serie_aud'
ALTER TABLE serie_aud
DROP COLUMN IF EXISTS turno;

-- Adicionando a coluna 'turno' na tabela 'turma_aud'
ALTER TABLE turma_aud
    ADD COLUMN turno VARCHAR(20);


