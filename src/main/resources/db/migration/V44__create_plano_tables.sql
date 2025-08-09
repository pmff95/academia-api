-- ============================
-- Migration 1: Tabelas principais
-- ============================

CREATE TABLE plano_disciplina (
    id BIGSERIAL PRIMARY KEY,
    turma_disciplina_id BIGINT,
    disciplina_grupo_id BIGINT,
    ano_letivo INTEGER NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_plano_disciplina_turma_disciplina FOREIGN KEY (turma_disciplina_id) REFERENCES turma_disciplina(id),
    CONSTRAINT fk_plano_disciplina_disciplina_grupo FOREIGN KEY (disciplina_grupo_id) REFERENCES disciplina_grupo_itinerario(id)
);

CREATE TABLE atividade (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    descricao TEXT,
    prazo DATE,
    plano_disciplina_id BIGINT NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_atividade_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id)
);

CREATE TABLE entrega_atividade (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    atividade_id BIGINT NOT NULL,
    entregue_em TIMESTAMP,
    arquivo_url TEXT,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_entrega_atividade_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id),
    CONSTRAINT fk_entrega_atividade_atividade FOREIGN KEY (atividade_id) REFERENCES atividade(id)
);

CREATE TABLE material_estudo (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    tipo VARCHAR(100),
    url TEXT,
    plano_disciplina_id BIGINT NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_material_estudo_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id)
);

CREATE TABLE avaliacao (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    tipo VARCHAR(100),
    data DATE,
    plano_disciplina_id BIGINT NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_avaliacao_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id)
);

CREATE TABLE nota (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    avaliacao_id BIGINT NOT NULL,
    valor NUMERIC(5,2),
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_nota_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id),
    CONSTRAINT fk_nota_avaliacao FOREIGN KEY (avaliacao_id) REFERENCES avaliacao(id)
);

CREATE TABLE resumo_aula (
    id BIGSERIAL PRIMARY KEY,
    data DATE,
    conteudo TEXT,
    plano_disciplina_id BIGINT NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_resumo_aula_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id)
);

CREATE TABLE aviso_disciplina (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(255),
    mensagem TEXT,
    data DATE,
    plano_disciplina_id BIGINT NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    version INT NOT NULL DEFAULT 0,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_aviso_disciplina_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id)
);