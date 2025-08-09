-- Altera o tipo da coluna
ALTER TABLE escola
ALTER COLUMN id TYPE BIGINT;

-- Garante que a sequence ainda tá vinculada (só se for necessário)
ALTER SEQUENCE escola_id_seq
    OWNED BY escola.id;
