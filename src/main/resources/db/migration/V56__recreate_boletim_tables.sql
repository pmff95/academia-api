-- ===============================================
-- Migration V56: Recreate boletim_aluno & plano_disciplina_periodo
-- ===============================================

-- 1. Drop old tables if they exist
DROP TABLE IF EXISTS plano_disciplina_periodo CASCADE;
DROP TABLE IF EXISTS boletim CASCADE;

-- 2. Create boletim_aluno
CREATE TABLE boletim_aluno (
                               id BIGSERIAL PRIMARY KEY,
                               aluno_id BIGINT NOT NULL,
                               ano_letivo INTEGER NOT NULL,
                               escola_id BIGINT NOT NULL,
                               observacao_geral TEXT,
                               uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                               criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                               atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
                               version INT NOT NULL DEFAULT 0,
                               CONSTRAINT fk_boletim_aluno_aluno FOREIGN KEY (aluno_id) REFERENCES aluno(id),
                               CONSTRAINT fk_boletim_aluno_escola FOREIGN KEY (escola_id) REFERENCES escola(id)
);

-- 3. Create plano_disciplina_periodo
CREATE TABLE plano_disciplina_periodo (
                                          id BIGSERIAL PRIMARY KEY,
                                          boletim_id BIGINT NOT NULL,
                                          plano_disciplina_id BIGINT NOT NULL,
                                          periodo_id BIGINT NOT NULL,
                                          nota DOUBLE PRECISION,
                                          faltas INTEGER,
                                          observacao TEXT,
                                          uuid UUID DEFAULT uuid_generate_v4() UNIQUE NOT NULL,
                                          criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                          atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          status VARCHAR(20) NOT NULL DEFAULT 'ATIVO',
                                          version INT NOT NULL DEFAULT 0,
                                          CONSTRAINT fk_plano_periodo_boletim FOREIGN KEY (boletim_id) REFERENCES boletim_aluno(id),
                                          CONSTRAINT fk_plano_periodo_plano_disciplina FOREIGN KEY (plano_disciplina_id) REFERENCES plano_disciplina(id),
                                          CONSTRAINT fk_plano_periodo_periodo FOREIGN KEY (periodo_id) REFERENCES periodo_avaliativo(id)
);