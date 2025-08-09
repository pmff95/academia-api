-- Adiciona colunas de confirmação e tipo de valor na tabela de auditoria
ALTER TABLE parametro_aud
    ADD COLUMN IF NOT EXISTS necessario_confirmacao BOOLEAN,
    ADD COLUMN IF NOT EXISTS mensagem_confirmacao_ativo VARCHAR(255),
    ADD COLUMN IF NOT EXISTS mensagem_confirmacao_inativo VARCHAR(255),
    ADD COLUMN IF NOT EXISTS mensagem_confirmacao_alteracao VARCHAR(255),
    ADD COLUMN IF NOT EXISTS tipo_valor VARCHAR(20);
