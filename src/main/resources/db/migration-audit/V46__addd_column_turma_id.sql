DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'audit'
          AND table_name = 'serie_horario_itinerario_aud'
          AND column_name = 'turma_id'
    ) THEN
ALTER TABLE audit.serie_horario_itinerario_aud
    ADD COLUMN turma_id BIGINT;
END IF;
END;
$$;
