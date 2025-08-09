-- Adiciona coluna de carga horaria em disciplina
ALTER TABLE disciplina
    ADD COLUMN carga_horaria INTEGER DEFAULT 0 NOT NULL;
