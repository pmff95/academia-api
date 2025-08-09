-- V69__alter_data_avaliacao_aud_to_date.sql

ALTER TABLE avaliacao_aud
    ALTER COLUMN data TYPE date USING data::date;
