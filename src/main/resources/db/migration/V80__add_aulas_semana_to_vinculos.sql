-- Adiciona coluna aulas_semana em turma_disciplina e disciplina_grupo_itinerario
ALTER TABLE turma_disciplina ADD COLUMN aulas_semana INTEGER DEFAULT 0 NOT NULL;

ALTER TABLE disciplina_grupo_itinerario ADD COLUMN aulas_semana INTEGER DEFAULT 0 NOT NULL;
