-- Adiciona coluna aulas_semana nas tabelas audit
ALTER TABLE audit.turma_disciplina_aud ADD COLUMN aulas_semana INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE audit.disciplina_grupo_itinerario_aud ADD COLUMN aulas_semana INTEGER DEFAULT 0 NOT NULL;
