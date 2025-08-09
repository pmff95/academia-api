-- Adiciona coluna status à tabela serie
ALTER TABLE serie
    ADD COLUMN status VARCHAR(20) CHECK (status IN ('ATIVO', 'INATIVO')) DEFAULT 'ATIVO' NOT NULL;
