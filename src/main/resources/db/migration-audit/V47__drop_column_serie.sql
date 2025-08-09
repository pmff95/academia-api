DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_schema = 'audit'
          AND table_name = 'serie_horario_itinerario_aud'
          AND column_name = 'serie'
    ) THEN
ALTER TABLE audit.serie_horario_itinerario_aud
DROP COLUMN serie;
END IF;
END;
$$;
