-- Ensure CPF uniqueness per school
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_cpf_key;
ALTER TABLE usuario ADD CONSTRAINT uk_usuario_escola_cpf UNIQUE (escola_id, cpf);
