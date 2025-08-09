ALTER TABLE audit.atividade_aud
ALTER
COLUMN status DROP
NOT NULL;

ALTER TABLE audit.entrega_atividade_aud
ALTER
COLUMN status DROP
NOT NULL;
