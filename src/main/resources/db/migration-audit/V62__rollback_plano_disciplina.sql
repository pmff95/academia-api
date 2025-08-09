ALTER TABLE plano_disciplina_aud DROP COLUMN IF EXISTS disciplina_id;
ALTER TABLE plano_disciplina_aud
    ADD COLUMN turma_disciplina_id BIGINT,
    ADD COLUMN disciplina_grupo_id BIGINT;
