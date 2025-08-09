-- Adicionando escola_id no itinerario_formativo_aud
ALTER TABLE itinerario_formativo_aud
    ADD COLUMN escola_id BIGINT;

-- Adicionando escola_id no grupo_itinerario_aud
ALTER TABLE grupo_itinerario_aud
    ADD COLUMN escola_id BIGINT;

-- Adicionando escola_id no disciplina_aud
ALTER TABLE disciplina_aud
    ADD COLUMN escola_id BIGINT;
