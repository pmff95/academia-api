-- Atualiza plano_disciplina para referenciar apenas disciplina
ALTER TABLE plano_disciplina
    ADD COLUMN disciplina_id BIGINT;

UPDATE plano_disciplina pd
SET disciplina_id = td.disciplina_id
FROM turma_disciplina td
WHERE pd.turma_disciplina_id = td.id;

UPDATE plano_disciplina pd
SET disciplina_id = dgi.disciplina_id
FROM disciplina_grupo_itinerario dgi
WHERE pd.disciplina_grupo_id = dgi.id
  AND pd.disciplina_id IS NULL;

ALTER TABLE plano_disciplina
    ADD CONSTRAINT fk_plano_disciplina_disciplina FOREIGN KEY (disciplina_id) REFERENCES disciplina(id);

ALTER TABLE plano_disciplina DROP CONSTRAINT fk_plano_disciplina_turma_disciplina;
ALTER TABLE plano_disciplina DROP CONSTRAINT fk_plano_disciplina_disciplina_grupo;
ALTER TABLE plano_disciplina DROP COLUMN turma_disciplina_id;
ALTER TABLE plano_disciplina DROP COLUMN disciplina_grupo_id;
