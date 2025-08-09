ALTER TABLE serie ADD COLUMN escola_id BIGINT;
ALTER TABLE serie ADD CONSTRAINT fk_serie_escola FOREIGN KEY (escola_id) REFERENCES escola(id);