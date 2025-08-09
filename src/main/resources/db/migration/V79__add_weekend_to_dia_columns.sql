-- V79__add_weekend_to_dia_columns.sql
ALTER TABLE horario_disponivel
    DROP CONSTRAINT IF EXISTS horario_disponivel_dia_semana_check,
    ADD CONSTRAINT horario_disponivel_dia_semana_check CHECK (dia_semana IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));

ALTER TABLE aula_horario
    DROP CONSTRAINT IF EXISTS aula_horario_dia_check,
    ADD CONSTRAINT aula_horario_dia_check CHECK (dia IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));

ALTER TABLE aula_horario_grade
    DROP CONSTRAINT IF EXISTS aula_horario_grade_dia_check,
    ADD CONSTRAINT aula_horario_grade_dia_check CHECK (dia IN ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));
