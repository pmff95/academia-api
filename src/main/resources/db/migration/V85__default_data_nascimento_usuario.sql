-- Define valor padrão e torna obrigatória a data de nascimento
UPDATE usuario SET data_nascimento = '1990-01-01' WHERE data_nascimento IS NULL;

ALTER TABLE usuario
    ALTER COLUMN data_nascimento SET DEFAULT '1990-01-01',
    ALTER COLUMN data_nascimento SET NOT NULL;
