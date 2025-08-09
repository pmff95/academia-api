-- V70__add_is_grupo_column_to_atividade.sql

ALTER TABLE atividade
    ADD COLUMN is_grupo BOOLEAN DEFAULT FALSE NOT NULL;
