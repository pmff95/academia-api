-- Renomeando a coluna 'status' para 'status_pedido' na tabela 'pedido'
ALTER TABLE pedido
RENAME COLUMN status TO status_pedido;