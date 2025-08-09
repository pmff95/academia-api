ALTER TABLE categoria_produto
    ALTER COLUMN id TYPE BIGINT;

ALTER TABLE categoria_produto
    ALTER COLUMN id SET DEFAULT nextval('categoria_produto_id_seq');

ALTER TABLE categoria_produto
DROP CONSTRAINT IF EXISTS fk_categoria_produto_escola;

ALTER TABLE categoria_produto
ALTER COLUMN escola_id TYPE BIGINT;

ALTER TABLE categoria_produto
    ADD CONSTRAINT fk_categoria_produto_escola
        FOREIGN KEY (escola_id) REFERENCES escola(id);

