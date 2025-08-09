-- Tabelas para a feature Parametro

-- Tabela principal de parametros globais
CREATE TABLE parametro (
    id BIGSERIAL PRIMARY KEY,
    chave VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(255),
    valor_default VARCHAR(255),
    uuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    version INT NOT NULL DEFAULT 0
);

-- Tabela que relaciona parametros com escolas
CREATE TABLE parametro_escola (
    id BIGSERIAL PRIMARY KEY,
    escola_id BIGINT NOT NULL,
    parametro_id BIGINT NOT NULL,
    valor_customizado VARCHAR(255),
    uuid UUID NOT NULL DEFAULT uuid_generate_v4() UNIQUE,
    criado_em TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    version INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_parametro_escola_escola FOREIGN KEY (escola_id) REFERENCES escola(id),
    CONSTRAINT fk_parametro_escola_parametro FOREIGN KEY (parametro_id) REFERENCES parametro(id),
    CONSTRAINT uk_parametro_escola UNIQUE (escola_id, parametro_id)
);

-- Adiciona coluna tipo na tabela parametro
ALTER TABLE parametro
    ADD COLUMN IF NOT EXISTS tipo VARCHAR(20) CHECK (tipo IN ('VISUAL','FUNCIONAL','ACADEMICO','FINANCEIRO','OUTRO')) DEFAULT 'OUTRO' NOT NULL;
