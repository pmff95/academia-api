-- Adiciona coluna tipo_periodo na tabela de auditoria da escola
ALTER TABLE escola_aud
    ADD COLUMN IF NOT EXISTS tipo_periodo VARCHAR(20);
