-- Criação da tabela de avisos
CREATE TABLE aviso (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    mensagem TEXT,
    usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
    criado_em TIMESTAMP NOT NULL DEFAULT NOW(),
    atualizado_em TIMESTAMP DEFAULT NOW(),
    version INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_aviso_uuid ON aviso(uuid);
CREATE INDEX idx_aviso_usuario ON aviso(usuario_id);
