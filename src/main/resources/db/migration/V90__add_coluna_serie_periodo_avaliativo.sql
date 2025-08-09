-- V90__add_coluna_serie_periodo_avaliativo.sql

ALTER TABLE periodo_avaliativo
    ADD COLUMN serie VARCHAR(50) NOT NULL;
