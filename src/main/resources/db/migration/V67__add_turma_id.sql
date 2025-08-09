DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name='serie_horario_itinerario'
          AND column_name='turma_id'
    ) THEN
ALTER TABLE public.serie_horario_itinerario
    ADD COLUMN turma_id BIGINT;
END IF;
END;
$$;
