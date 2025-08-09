-- Adiciona a coluna escola_id nas tabelas de horários
ALTER TABLE serie_horario_aula
    ADD COLUMN escola_id BIGINT NOT NULL DEFAULT 11,
ADD CONSTRAINT fk_serie_horario_aula_escola
    FOREIGN KEY (escola_id) REFERENCES escola(id)
        ON DELETE CASCADE;

ALTER TABLE serie_horario_itinerario
    ADD COLUMN escola_id BIGINT NOT NULL DEFAULT 11,
ADD CONSTRAINT fk_serie_horario_itinerario_escola
    FOREIGN KEY (escola_id) REFERENCES escola(id)
        ON DELETE CASCADE;
