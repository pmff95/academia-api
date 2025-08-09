-- Adiciona colunas de confirmação e tipo de valor na tabela parametro
ALTER TABLE parametro
    ADD COLUMN IF NOT EXISTS necessario_confirmacao BOOLEAN DEFAULT FALSE,
    ADD COLUMN IF NOT EXISTS mensagem_confirmacao_ativo VARCHAR(255),
    ADD COLUMN IF NOT EXISTS mensagem_confirmacao_inativo VARCHAR(255),
    ADD COLUMN IF NOT EXISTS mensagem_confirmacao_alteracao VARCHAR(255),
    ADD COLUMN IF NOT EXISTS tipo_valor VARCHAR(20) CHECK (tipo_valor IN ('TEXTO','BOOLEANO')) DEFAULT 'TEXTO' NOT NULL;
