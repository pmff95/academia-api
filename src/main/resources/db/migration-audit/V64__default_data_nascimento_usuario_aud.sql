-- Ajusta valor padrão e obrigatoriedade da data de nascimento
UPDATE usuario_aud SET data_nascimento = '1990-01-01' WHERE data_nascimento IS NULL;

ALTER TABLE usuario_aud
    ALTER COLUMN data_nascimento SET DEFAULT '1990-01-01',
    ALTER COLUMN data_nascimento SET NOT NULL;
