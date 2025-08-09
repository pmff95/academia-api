DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'audit'
          AND table_name = 'aula_horario_grade_aud'
          AND column_name = 'grupo_itinerario_id'
    ) THEN
ALTER TABLE audit.aula_horario_grade_aud
    ADD COLUMN grupo_itinerario_id UUID;
END IF;
END;
$$;
