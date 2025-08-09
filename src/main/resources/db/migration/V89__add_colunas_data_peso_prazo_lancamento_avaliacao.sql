-- V89__add_colunas_data_peso_prazo_lancamento_avaliacao.sql

ALTER TABLE avaliacao
ALTER COLUMN data TYPE timestamp USING data::timestamp;

ALTER TABLE avaliacao
    ADD COLUMN peso numeric,
    ADD COLUMN prazo_lancamento_notas date;
