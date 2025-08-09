DROP TABLE IF EXISTS controle_matricula_aud;

CREATE TABLE controle_matricula_aud (
                                              ano           INTEGER     NOT NULL,
                                              tipo          VARCHAR(35) NOT NULL,
                                              escola_id     BIGINT      NOT NULL,
                                              rev           INT         NOT NULL,
                                              revtype       SMALLINT,
                                              numero_atual  INTEGER,
                                              uuid          UUID,
                                              criado_em     TIMESTAMP,
                                              atualizado_em TIMESTAMP,
                                              PRIMARY KEY (ano, tipo, escola_id, rev),
                                              CONSTRAINT fk_ctrl_matr_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE,
                                              CONSTRAINT chk_ctrl_matr_aud_tipo_valid CHECK (tipo IN ('ALUNO', 'PROFESSOR', 'FUNCIONARIO', 'RESPONSAVEL'))
);
