-- V67__add_colunas_data_peso_prazo_lancamento_avaliacao_aud.sql

ALTER TABLE avaliacao_aud
ALTER COLUMN data TYPE TIMESTAMP;

ALTER TABLE avaliacao_aud
    ADD COLUMN peso NUMERIC,
    ADD COLUMN prazo_lancamento_notas DATE;

