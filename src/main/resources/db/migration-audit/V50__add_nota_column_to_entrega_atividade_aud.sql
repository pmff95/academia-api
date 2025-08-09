-- V50__add_nota_column_to_entrega_atividade_aud.sql

ALTER TABLE audit.entrega_atividade_aud
    ADD COLUMN nota DOUBLE PRECISION;
