-- V72__add_carga_horaria_to_vinculos.sql
ALTER TABLE turma_disciplina
    ADD COLUMN carga_horaria INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE disciplina_grupo_itinerario
    ADD COLUMN carga_horaria INTEGER DEFAULT 0 NOT NULL;
