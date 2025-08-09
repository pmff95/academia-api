CREATE TABLE aula_horario_grade_aud
(
    id                  BIGINT NOT NULL,
    rev                 INT    NOT NULL,
    revtype             SMALLINT,
    uuid                UUID,
    dia                 VARCHAR(20),
    inicio              TIME,
    fim                 TIME,
    turma_id            BIGINT,
    disciplina_id       BIGINT,
    grupo_itinerario_id BIGINT,
    criado_em           TIMESTAMP,
    atualizado_em       TIMESTAMP,
    status              VARCHAR(20),
    version             INTEGER,
    PRIMARY KEY (id, rev),
    CONSTRAINT fk_aula_horario_grade_aud_rev FOREIGN KEY (rev) REFERENCES revinfo (id) ON DELETE CASCADE
);
