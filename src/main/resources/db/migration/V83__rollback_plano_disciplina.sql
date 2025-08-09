ALTER TABLE plano_disciplina DROP CONSTRAINT IF EXISTS fk_plano_disciplina_disciplina;

ALTER TABLE plano_disciplina DROP COLUMN IF EXISTS disciplina_id;

ALTER TABLE plano_disciplina
    ADD COLUMN turma_disciplina_id BIGINT,
    ADD COLUMN disciplina_grupo_id BIGINT;

ALTER TABLE plano_disciplina
    ADD CONSTRAINT fk_plano_disciplina_turma_disciplina
        FOREIGN KEY (turma_disciplina_id) REFERENCES turma_disciplina(id),

    ADD CONSTRAINT fk_plano_disciplina_disciplina_grupo
        FOREIGN KEY (disciplina_grupo_id) REFERENCES disciplina_grupo_itinerario(id);
