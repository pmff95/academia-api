-- Adicionando escola ao itinerario_formativo
ALTER TABLE itinerario_formativo
    ADD COLUMN escola_id BIGINT NOT NULL,
ADD CONSTRAINT fk_itinerario_formativo_escola
    FOREIGN KEY (escola_id) REFERENCES escola(id)
    ON DELETE CASCADE;

-- Adicionando escola ao grupo_itinerario
ALTER TABLE grupo_itinerario
    ADD COLUMN escola_id BIGINT NOT NULL,
ADD CONSTRAINT fk_grupo_itinerario_escola
    FOREIGN KEY (escola_id) REFERENCES escola(id)
    ON DELETE CASCADE;

ALTER TABLE disciplina
    ADD COLUMN escola_id BIGINT NOT NULL,
ADD CONSTRAINT fk_disciplina_escola
    FOREIGN KEY (escola_id) REFERENCES escola(id)
    ON DELETE CASCADE;