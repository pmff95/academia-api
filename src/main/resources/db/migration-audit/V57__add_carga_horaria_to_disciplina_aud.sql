-- Adiciona coluna de carga horaria em disciplina_aud
ALTER TABLE audit.disciplina_aud
    ADD COLUMN carga_horaria INTEGER DEFAULT 0 NOT NULL;
