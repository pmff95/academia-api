-- Cria tabelas de auditoria para chamada, históricos, boletim e período avaliativo

CREATE TABLE registro_chamada_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    plano_disciplina_id BIGINT,
    professor_id BIGINT,
    data_aula DATE,
    aula_dupla BOOLEAN,
    preenchido BOOLEAN,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_registro_chamada_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE item_chamada_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    registro_chamada_id BIGINT,
    aluno_id BIGINT,
    presente BOOLEAN,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_item_chamada_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE historico_escolar_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    aluno_id BIGINT,
    ano INTEGER,
    serie VARCHAR(255),
    turma VARCHAR(255),
    disciplina VARCHAR(255),
    nota_final DOUBLE PRECISION,
    frequencia DOUBLE PRECISION,
    resultado_final VARCHAR(255),
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_historico_escolar_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE historico_professor_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    professor_id BIGINT,
    disciplina VARCHAR(255),
    ano_letivo INTEGER,
    total_horas INTEGER,
    turma_nome VARCHAR(255),
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_historico_professor_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE periodo_avaliativo_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    escola_id BIGINT,
    ano_letivo INTEGER,
    nome VARCHAR(255),
    ordem INTEGER,
    inicio DATE,
    fim DATE,
    gerado_automaticamente BOOLEAN,
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_periodo_avaliativo_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE boletim_aud (
    id BIGINT NOT NULL,
    rev INT NOT NULL,
    revtype SMALLINT,
    aluno_id BIGINT,
    plano_disciplina_id BIGINT,
    periodo_id BIGINT,
    nota DOUBLE PRECISION,
    faltas INTEGER,
    observacao VARCHAR(255),
    uuid UUID,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    status VARCHAR(20),
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_boletim_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id) ON DELETE CASCADE
);
