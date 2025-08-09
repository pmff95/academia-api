-- Cria tabelas de chamada, históricos, boletim e período avaliativo

CREATE TABLE registro_chamada (
    id BIGSERIAL PRIMARY KEY,
    plano_disciplina_id BIGINT NOT NULL,
    professor_id BIGINT NOT NULL,
    data_aula DATE NOT NULL,
    aula_dupla BOOLEAN NOT NULL DEFAULT FALSE,
    preenchido BOOLEAN NOT NULL DEFAULT FALSE,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_registro_chamada_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id),
    CONSTRAINT fk_registro_chamada_professor FOREIGN KEY (professor_id) REFERENCES professor(id)
);

CREATE TABLE item_chamada (
    id BIGSERIAL PRIMARY KEY,
    registro_chamada_id BIGINT NOT NULL,
    aluno_id BIGINT NOT NULL,
    presente BOOLEAN NOT NULL,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_item_chamada_registro FOREIGN KEY (registro_chamada_id) REFERENCES registro_chamada(id) ON DELETE CASCADE,
    CONSTRAINT fk_item_chamada_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id) ON DELETE CASCADE
);

CREATE TABLE historico_escolar (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    ano INT,
    serie VARCHAR(255),
    turma VARCHAR(255),
    disciplina VARCHAR(255),
    nota_final DOUBLE PRECISION,
    frequencia DOUBLE PRECISION,
    resultado_final VARCHAR(255),
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_historico_escolar_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id)
);

CREATE TABLE historico_professor (
    id BIGSERIAL PRIMARY KEY,
    professor_id BIGINT NOT NULL,
    disciplina VARCHAR(255),
    ano_letivo INT,
    total_horas INT,
    turma_nome VARCHAR(255),
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_historico_professor_professor FOREIGN KEY (professor_id) REFERENCES professor(id)
);

CREATE TABLE periodo_avaliativo (
    id BIGSERIAL PRIMARY KEY,
    escola_id BIGINT NOT NULL,
    ano_letivo INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    ordem INT NOT NULL,
    inicio DATE,
    fim DATE,
    gerado_automaticamente BOOLEAN NOT NULL DEFAULT TRUE,
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_periodo_avaliativo_escola FOREIGN KEY (escola_id) REFERENCES escola(id)
);

CREATE TABLE boletim (
    id BIGSERIAL PRIMARY KEY,
    aluno_id BIGINT NOT NULL,
    plano_disciplina_id BIGINT NOT NULL,
    periodo_id BIGINT NOT NULL,
    nota DOUBLE PRECISION,
    faltas INT,
    observacao VARCHAR(255),
    uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_boletim_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id),
    CONSTRAINT fk_boletim_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id),
    CONSTRAINT fk_boletim_periodo FOREIGN KEY (periodo_id) REFERENCES periodo_avaliativo(id)
);
