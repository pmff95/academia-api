-- Renomeando a coluna 'status' para 'status_pagamento' na tabela 'pagamento'
ALTER TABLE pagamento
    RENAME COLUMN status TO status_pagamento;