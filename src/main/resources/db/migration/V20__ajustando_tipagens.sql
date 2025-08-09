ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_escola_id_fkey;
ALTER TABLE categoria_produto DROP CONSTRAINT IF EXISTS categoria_produto_escola_id_fkey;
ALTER TABLE produto DROP CONSTRAINT IF EXISTS fk_produto_escola;
ALTER TABLE produto DROP CONSTRAINT IF EXISTS fk_produto_categoria;
ALTER TABLE pedido DROP CONSTRAINT IF EXISTS pedido_escola_id_fkey;
ALTER TABLE pedido DROP CONSTRAINT IF EXISTS pedido_vendedor_id_fkey;
ALTER TABLE escola_financeiro DROP CONSTRAINT IF EXISTS escola_financeiro_escola_id_fkey;
ALTER TABLE escola_endereco DROP CONSTRAINT IF EXISTS fk_escola_info_escola;
ALTER TABLE escola_modulo DROP CONSTRAINT IF EXISTS escola_modulo_escola_id_fkey;
ALTER TABLE aluno DROP CONSTRAINT IF EXISTS fk_aluno_id;
ALTER TABLE responsavel_aluno DROP CONSTRAINT IF EXISTS fk_responsavel_usuario;
ALTER TABLE responsavel_aluno DROP CONSTRAINT IF EXISTS fk_aluno_usuario;
ALTER TABLE professor DROP CONSTRAINT IF EXISTS fk_professor_id;
ALTER TABLE transacao DROP CONSTRAINT IF EXISTS fk_transacao_usuario;
ALTER TABLE endereco DROP CONSTRAINT IF EXISTS fk_endereco_usuario;
ALTER TABLE pagamento DROP CONSTRAINT IF EXISTS pagamento_usuario_id_fkey;

-- Ajustando tipos das colunas
ALTER TABLE escola ALTER COLUMN id TYPE BIGINT;
ALTER TABLE usuario ALTER COLUMN id TYPE BIGINT;
ALTER TABLE categoria_produto ALTER COLUMN id TYPE BIGINT;
ALTER TABLE responsavel_aluno ALTER COLUMN id TYPE BIGINT;
ALTER TABLE escola_financeiro ALTER COLUMN id TYPE BIGINT;

ALTER TABLE usuario ALTER COLUMN escola_id TYPE BIGINT;
ALTER TABLE categoria_produto ALTER COLUMN escola_id TYPE BIGINT;
ALTER TABLE produto ALTER COLUMN escola_id TYPE BIGINT;
ALTER TABLE pedido ALTER COLUMN escola_id TYPE BIGINT;
ALTER TABLE pedido ALTER COLUMN vendedor_id TYPE BIGINT;
ALTER TABLE escola_financeiro ALTER COLUMN escola_id TYPE BIGINT;

-- Recriando constraints removidas
ALTER TABLE usuario ADD CONSTRAINT usuario_escola_id_fkey FOREIGN KEY (escola_id) REFERENCES escola(id) ON DELETE CASCADE;
ALTER TABLE categoria_produto ADD CONSTRAINT categoria_produto_escola_id_fkey FOREIGN KEY (escola_id) REFERENCES escola(id);
ALTER TABLE produto ADD CONSTRAINT fk_produto_escola FOREIGN KEY (escola_id) REFERENCES escola(id) ON DELETE CASCADE;
ALTER TABLE produto ADD CONSTRAINT fk_produto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria_produto(id) ON DELETE CASCADE;
ALTER TABLE pedido ADD CONSTRAINT pedido_escola_id_fkey FOREIGN KEY (escola_id) REFERENCES escola(id);
ALTER TABLE pedido ADD CONSTRAINT pedido_vendedor_id_fkey FOREIGN KEY (vendedor_id) REFERENCES usuario(id);
ALTER TABLE escola_financeiro ADD CONSTRAINT escola_financeiro_escola_id_fkey FOREIGN KEY (escola_id) REFERENCES escola(id) ON DELETE CASCADE;
ALTER TABLE escola_endereco ADD CONSTRAINT fk_escola_info_escola FOREIGN KEY (escola_id) REFERENCES escola(id) ON DELETE CASCADE;
ALTER TABLE escola_modulo ADD CONSTRAINT escola_modulo_escola_id_fkey FOREIGN KEY (escola_id) REFERENCES escola(id) ON DELETE CASCADE;
ALTER TABLE aluno ADD CONSTRAINT fk_aluno_id FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE NO ACTION;
ALTER TABLE responsavel_aluno ADD CONSTRAINT fk_responsavel_usuario FOREIGN KEY (responsavel_id) REFERENCES usuario(id) ON DELETE CASCADE;
ALTER TABLE responsavel_aluno ADD CONSTRAINT fk_aluno_usuario FOREIGN KEY (aluno_id) REFERENCES usuario(id) ON DELETE CASCADE;
ALTER TABLE professor ADD CONSTRAINT fk_professor_id FOREIGN KEY (id) REFERENCES usuario(id) ON DELETE NO ACTION;
ALTER TABLE transacao ADD CONSTRAINT fk_transacao_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE NO ACTION;
ALTER TABLE endereco ADD CONSTRAINT fk_endereco_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE;
ALTER TABLE pagamento ADD CONSTRAINT pagamento_usuario_id_fkey FOREIGN KEY (usuario_id) REFERENCES usuario(id);