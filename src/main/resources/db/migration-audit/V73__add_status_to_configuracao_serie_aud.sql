-- V73__add_status_to_configuracao_serie_aud.sql

ALTER TABLE configuracao_serie_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
