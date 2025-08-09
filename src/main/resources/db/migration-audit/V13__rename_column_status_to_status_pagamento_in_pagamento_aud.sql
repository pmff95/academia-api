-- Renomeando a coluna 'status' para 'status_pagamento' na tabela 'pagamento'
ALTER TABLE pagamento_aud
RENAME COLUMN status TO status_pagamento;