DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name = 'serie_horario_itinerario'
          AND column_name = 'serie'
    ) THEN
ALTER TABLE serie_horario_itinerario
DROP COLUMN serie;
END IF;
END;
$$;
