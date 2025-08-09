-- Renomeando a coluna 'status' para 'status_pedido' na tabela 'pedido'
ALTER TABLE pedido_aud
RENAME COLUMN status TO status_pedido;