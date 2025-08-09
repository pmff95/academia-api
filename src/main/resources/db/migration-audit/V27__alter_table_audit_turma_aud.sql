-- V27__alter_table_audit_turma_aud.sql
-- Descrição: Adiciona as colunas 'serie' e 'ano_letivo' na tabela de auditoria 'turma_aud'
-- Sem constraints

-- Adiciona coluna 'serie' como VARCHAR
ALTER TABLE turma_aud
    ADD COLUMN serie VARCHAR(30);

-- Adiciona coluna 'ano_letivo' como INTEGER
ALTER TABLE turma_aud
    ADD COLUMN ano_letivo INTEGER;
