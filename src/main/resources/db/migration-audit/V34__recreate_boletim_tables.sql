-- ===============================================
-- Migration V34: Recreate audit tables for boletim_aluno & plano_disciplina_periodo
-- ===============================================

-- 1. Drop old audit tables if they exist
DROP TABLE IF EXISTS plano_disciplina_periodo_aud CASCADE;
DROP TABLE IF EXISTS boletim_aluno_aud CASCADE;
DROP TABLE IF EXISTS boletim_aud CASCADE;

-- 2. Create boletim_aluno_aud
CREATE TABLE boletim_aluno_aud (
                                   id BIGINT NOT NULL,
                                   rev INTEGER NOT NULL,
                                   revtype SMALLINT,
                                   aluno_id BIGINT,
                                   ano_letivo INTEGER,
                                   escola_id BIGINT,
                                   observacao_geral TEXT,
                                   uuid UUID,
                                   criado_em TIMESTAMP,
                                   atualizado_em TIMESTAMP,
                                   status VARCHAR(20),
                                   version INT,
                                   PRIMARY KEY (id, rev),
                                   CONSTRAINT fk_boletim_aluno_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);

-- 3. Create plano_disciplina_periodo_aud
CREATE TABLE plano_disciplina_periodo_aud (
                                              id BIGINT NOT NULL,
                                              rev INTEGER NOT NULL,
                                              revtype SMALLINT,
                                              boletim_id BIGINT,
                                              plano_disciplina_id BIGINT,
                                              periodo_id BIGINT,
                                              nota DOUBLE PRECISION,
                                              faltas INTEGER,
                                              observacao TEXT,
                                              uuid UUID,
                                              criado_em TIMESTAMP,
                                              atualizado_em TIMESTAMP,
                                              status VARCHAR(20),
                                              version INT,
                                              PRIMARY KEY (id, rev),
                                              CONSTRAINT fk_plano_disciplina_periodo_aud_rev FOREIGN KEY (rev) REFERENCES revinfo(id)
);