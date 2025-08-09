-- Adiciona coluna escola_id nas tabelas de auditoria
ALTER TABLE serie_horario_aula_aud
    ADD COLUMN escola_id BIGINT;

ALTER TABLE serie_horario_itinerario_aud
    ADD COLUMN escola_id BIGINT;
