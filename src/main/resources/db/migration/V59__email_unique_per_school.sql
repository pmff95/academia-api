-- Ensure email uniqueness per school
ALTER TABLE usuario DROP CONSTRAINT IF EXISTS usuario_email_key;
DROP INDEX IF EXISTS idx_usuario_email;
ALTER TABLE usuario ADD CONSTRAINT uk_usuario_escola_email UNIQUE (escola_id, email);
