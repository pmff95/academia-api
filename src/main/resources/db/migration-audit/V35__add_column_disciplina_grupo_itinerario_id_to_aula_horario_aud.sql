-- Adiciona a coluna se não existir
ALTER TABLE aula_horario_aud
    ADD COLUMN IF NOT EXISTS disciplina_grupo_itinerario_id BIGINT;

-- Evita erro ao tentar adicionar constraint já existente
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_aula_horario_aud_disciplina_grupo_itinerario'
          AND table_name = 'aula_horario_aud'
    ) THEN
ALTER TABLE aula_horario_aud
    ADD CONSTRAINT fk_aula_horario_aud_disciplina_grupo_itinerario
        FOREIGN KEY (disciplina_grupo_itinerario_id)
            REFERENCES disciplina_grupo_itinerario(id)
            ON DELETE CASCADE;
END IF;
END
$$;
