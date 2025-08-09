-- V95__add_status_to_configuracao_serie.sql

ALTER TABLE configuracao_serie
    ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ATIVO';
