-- V91__alter_data_avaliacao_to_date.sql

ALTER TABLE avaliacao
    ALTER COLUMN data TYPE date USING data::date;
