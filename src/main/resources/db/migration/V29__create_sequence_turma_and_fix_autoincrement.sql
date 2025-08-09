-- Criar sequência (se não existir)
CREATE SEQUENCE turma_id_seq START WITH 1 INCREMENT BY 1;

-- Alterar coluna id para usar a sequência
ALTER TABLE turma ALTER COLUMN id SET DEFAULT nextval('turma_id_seq');