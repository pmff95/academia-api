-- TABELA PAGAMENTO
-- ==========================
CREATE TABLE pagamento (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id),
    mp_id VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    data TIMESTAMP,
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_pagamento_uuid ON pagamento(uuid);
CREATE INDEX idx_pagamento_usuario ON pagamento(usuario_id);

-- TABELA PAGAMENTO_ITEM
-- ==========================
CREATE TABLE pagamento_item (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID UNIQUE DEFAULT uuid_generate_v4(),
    pagamento_id BIGINT NOT NULL REFERENCES pagamento(id) ON DELETE CASCADE,
    aluno_id BIGINT NOT NULL REFERENCES aluno(id) ON DELETE CASCADE,
    tipo VARCHAR(50),
    titulo VARCHAR(255),
    criado_em TIMESTAMP DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW()
);

CREATE INDEX idx_pagamento_item_pagamento ON pagamento_item(pagamento_id);
CREATE INDEX idx_pagamento_item_aluno ON pagamento_item(aluno_id);
