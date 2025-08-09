-- V37__remover_not_null_status_plano_disciplina_aud.sql

-- Remove a restrição NOT NULL da coluna 'status' na tabela de auditoria 'plano_disciplina_aud'
ALTER TABLE audit.plano_disciplina_aud
    ALTER COLUMN status DROP NOT NULL;
