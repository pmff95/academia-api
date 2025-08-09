-- Adiciona coluna tipo_periodo na tabela escola
ALTER TABLE escola
    ADD COLUMN IF NOT EXISTS tipo_periodo VARCHAR(20);
