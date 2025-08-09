-- Altera o tipo da coluna 'data_nascimento' para DATE
ALTER TABLE usuario_aud
    ALTER COLUMN data_nascimento TYPE DATE
    USING data_nascimento::DATE;
