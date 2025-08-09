-- Adds status column to audit tables matching BaseEntity changes
ALTER TABLE turma_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE aluno_grupo_itinerario_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE disciplina_grupo_itinerario_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE grupo_itinerario_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE itinerario_formativo_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE carteira_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE transacao_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE controle_matricula_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE horario_disponivel_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE endereco_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
ALTER TABLE pagamento_item_aud
    ADD COLUMN IF NOT EXISTS status VARCHAR(20);
