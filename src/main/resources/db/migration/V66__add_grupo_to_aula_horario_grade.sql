ALTER TABLE aula_horario_grade
    ADD COLUMN grupo_itinerario_id BIGINT;

ALTER TABLE aula_horario_grade
    ADD CONSTRAINT fk_aula_horario_grade_grupo_itinerario FOREIGN KEY (grupo_itinerario_id) REFERENCES grupo_itinerario(id) ON DELETE CASCADE;

CREATE INDEX idx_aula_horario_grade_grupo ON aula_horario_grade(grupo_itinerario_id);
