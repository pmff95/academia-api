DROP TABLE grade_horario_aud;

CREATE TABLE turma_disciplina_aud (
                                         id            BIGINT      NOT NULL,
                                         rev           INT         NOT NULL,
                                         revtype       SMALLINT,
                                         uuid          UUID,
                                         turma_id      BIGINT,
                                         professor_id  BIGINT,
                                         disciplina_id BIGINT,
                                         criado_em     TIMESTAMP,
                                         atualizado_em TIMESTAMP,
                                         version       INTEGER,
                                         PRIMARY KEY (id, rev),
                                         CONSTRAINT fk_grd_hor_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);

CREATE TABLE itinerario_formativo_aud (
                                                id            BIGINT      NOT NULL,
                                                rev           INT         NOT NULL,
                                                revtype       SMALLINT,
                                                uuid          UUID,
                                                nome          VARCHAR(255),
                                                descricao     TEXT,
                                                criado_em     TIMESTAMP,
                                                atualizado_em TIMESTAMP,
                                                PRIMARY KEY (id, rev),
                                                CONSTRAINT fk_itinerario_formativo_aud_rev FOREIGN KEY (rev)
                                                    REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE grupo_itinerario_aud (
                                            id            BIGINT      NOT NULL,
                                            rev           INT         NOT NULL,
                                            revtype       SMALLINT,
                                            uuid          UUID,
                                            nome          VARCHAR(255),
                                            ano_letivo    INTEGER,
                                            itinerario_id BIGINT,
                                            criado_em     TIMESTAMP,
                                            atualizado_em TIMESTAMP,
                                            PRIMARY KEY (id, rev),
                                            CONSTRAINT fk_grupo_itinerario_aud_rev FOREIGN KEY (rev)
                                                REFERENCES revinfo(id) ON DELETE CASCADE
);


CREATE TABLE aluno_grupo_itinerario_aud (
                                                  id         BIGINT      NOT NULL,
                                                  rev        INT         NOT NULL,
                                                  revtype    SMALLINT,
                                                  uuid       UUID,
                                                  aluno_id   BIGINT,
                                                  grupo_id   BIGINT,
                                                  criado_em  TIMESTAMP,
                                                  atualizado_em TIMESTAMP,
                                                  PRIMARY KEY (id, rev),
                                                  CONSTRAINT fk_aluno_grupo_itinerario_aud_rev FOREIGN KEY (rev)
                                                      REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE disciplina_grupo_itinerario_aud (
                                                      id             BIGINT      NOT NULL,
                                                      rev            INT         NOT NULL,
                                                      revtype        SMALLINT,
                                                      uuid           UUID,
                                                      grupo_id       BIGINT,
                                                      disciplina_id  BIGINT,
                                                      professor_id   BIGINT,
                                                      criado_em      TIMESTAMP,
                                                      atualizado_em  TIMESTAMP,
                                                      PRIMARY KEY (id, rev),
                                                      CONSTRAINT fk_disciplina_grupo_itinerario_aud_rev FOREIGN KEY (rev)
                                                          REFERENCES revinfo(id) ON DELETE CASCADE
);

CREATE TABLE aula_horario_aud (
                                        id            BIGINT      NOT NULL,
                                        rev           INT         NOT NULL,
                                        revtype       SMALLINT,
                                        uuid          UUID,
                                        dia           VARCHAR(20),
                                        inicio        TIME,
                                        fim           TIME,
                                        turma_disciplina_id BIGINT,
                                        disciplina_grupo_itinerario_id BIGINT,
                                        criado_em     TIMESTAMP,
                                        atualizado_em TIMESTAMP,
                                        version       INTEGER,
                                        PRIMARY KEY (id, rev),
                                        CONSTRAINT fk_grd_hor_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);


