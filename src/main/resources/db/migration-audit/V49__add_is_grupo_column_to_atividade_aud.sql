-- V49__add_is_grupo_column_to_atividade_aud.sql

ALTER TABLE audit.atividade_aud
    ADD COLUMN is_grupo BOOLEAN DEFAULT FALSE NOT NULL;
