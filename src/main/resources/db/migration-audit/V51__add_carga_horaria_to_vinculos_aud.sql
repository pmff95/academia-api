-- V51__add_carga_horaria_to_vinculos_aud.sql
ALTER TABLE audit.turma_disciplina_aud
    ADD COLUMN carga_horaria INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE audit.disciplina_grupo_itinerario_aud
    ADD COLUMN carga_horaria INTEGER DEFAULT 0 NOT NULL;
