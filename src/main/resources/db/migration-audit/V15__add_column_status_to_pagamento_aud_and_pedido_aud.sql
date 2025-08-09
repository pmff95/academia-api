-- Adicionando coluna 'status' na tabela 'pagamento'
ALTER TABLE pagamento_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);

-- Adicionando coluna 'status' na tabela 'pedido'
ALTER TABLE pedido_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
