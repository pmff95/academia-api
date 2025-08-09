-- ============================
-- Migration 2: Tabelas de auditoria
-- ============================

CREATE TABLE plano_disciplina_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    turma_disciplina_id BIGINT,
    disciplina_grupo_id BIGINT,
    ano_letivo INTEGER,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_plano_disciplina_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE atividade_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    titulo VARCHAR(255),
    descricao TEXT,
    prazo DATE,
    plano_disciplina_id BIGINT,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_atividade_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE entrega_atividade_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    aluno_id BIGINT,
    atividade_id BIGINT,
    entregue_em TIMESTAMP,
    arquivo_url TEXT,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_entrega_atividade_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE material_estudo_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    titulo VARCHAR(255),
    tipo VARCHAR(100),
    url TEXT,
    plano_disciplina_id BIGINT,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_material_estudo_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE avaliacao_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    titulo VARCHAR(255),
    tipo VARCHAR(100),
    data DATE,
    plano_disciplina_id BIGINT,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_avaliacao_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE nota_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    aluno_id BIGINT,
    avaliacao_id BIGINT,
    valor NUMERIC(5,2),
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_nota_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE resumo_aula_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    data DATE,
    conteudo TEXT,
    plano_disciplina_id BIGINT,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_resumo_aula_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

CREATE TABLE aviso_disciplina_aud (
    id BIGINT NOT NULL,
    rev INTEGER NOT NULL,
    revtype SMALLINT,
    titulo VARCHAR(255),
    mensagem TEXT,
    data DATE,
    plano_disciplina_id BIGINT,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    version INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_aviso_disciplina_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);