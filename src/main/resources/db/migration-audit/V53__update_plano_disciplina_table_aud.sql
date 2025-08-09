-- Atualiza a tabela de auditoria de plano_disciplina para refletir nova estrutura
ALTER TABLE audit.plano_disciplina_aud
    ADD COLUMN disciplina_id BIGINT;

ALTER TABLE audit.plano_disciplina_aud
    DROP COLUMN turma_disciplina_id,
    DROP COLUMN disciplina_grupo_id;
