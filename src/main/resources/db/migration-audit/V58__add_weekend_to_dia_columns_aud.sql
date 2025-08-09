-- V58__add_weekend_to_dia_columns_aud.sql
ALTER TABLE audit.horario_disponivel_aud
    DROP CONSTRAINT IF EXISTS horario_disponivel_aud_dia_semana_check,
    ADD CONSTRAINT horario_disponivel_aud_dia_semana_check CHECK (dia_semana IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));

ALTER TABLE audit.aula_horario_aud
    DROP CONSTRAINT IF EXISTS aula_horario_aud_dia_check,
    ADD CONSTRAINT aula_horario_aud_dia_check CHECK (dia IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));

ALTER TABLE audit.aula_horario_grade_aud
    DROP CONSTRAINT IF EXISTS aula_horario_grade_aud_dia_check,
    ADD CONSTRAINT aula_horario_grade_aud_dia_check CHECK (dia IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));
