-- Adicionando coluna 'status' na tabela 'pagamento'
ALTER TABLE pagamento
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ATIVO';

-- Adicionando coluna 'status' na tabela 'pedido'
ALTER TABLE pedido
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ATIVO';
