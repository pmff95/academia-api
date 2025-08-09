-- V13_create_table_endereco.sql

-- Criação da tabela ENDEREÇO
CREATE TABLE endereco (
                          id BIGSERIAL PRIMARY KEY,

    -- Campos herdados de BaseEntity
                          uuid UUID NOT NULL DEFAULT uuid_generate_v4(),
                          criado_em TIMESTAMP DEFAULT NOW() NOT NULL,
                          atualizado_em TIMESTAMP DEFAULT NOW(),

    -- Campos próprios do endereço
                          cep VARCHAR(9) NOT NULL,
                          endereco VARCHAR(255) NOT NULL,
                          numero VARCHAR(20) NOT NULL,
                          cidade VARCHAR(100) NOT NULL,
                          estado VARCHAR(2) NOT NULL,

    -- Relacionamento com usuário
                          usuario_id BIGINT NOT NULL, -- Cada usuário só pode ter um endereço

                          CONSTRAINT fk_endereco_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
);

-- Índices para otimizar busca
CREATE INDEX idx_endereco_id ON endereco(id);
CREATE INDEX idx_endereco_uuid ON endereco(uuid);
CREATE INDEX idx_endereco_usuario ON endereco(usuario_id);
