ALTER TABLE usuario
ALTER COLUMN data_nascimento TYPE DATE
    USING data_nascimento::DATE;
