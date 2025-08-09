-- V71__add_nota_column_to_entrega_atividade.sql

ALTER TABLE entrega_atividade
    ADD COLUMN nota DOUBLE PRECISION;
