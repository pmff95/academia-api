ALTER TABLE audit.aula_horario_grade_aud
    DROP COLUMN grupo_itinerario_id;

ALTER TABLE audit.aula_horario_grade_aud
    ADD COLUMN grupo_itinerario_id BIGINT;
