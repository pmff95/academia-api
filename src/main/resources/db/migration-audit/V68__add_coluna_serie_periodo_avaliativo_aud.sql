-- V68__add_coluna_serie_periodo_avaliativo_aud.sql

ALTER TABLE periodo_avaliativo_aud
    ADD COLUMN serie VARCHAR(50);
