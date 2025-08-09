-- Adicionando coluna 'data_nascimento' na tabela 'usuario'
ALTER TABLE usuario_aud
    ADD COLUMN IF NOT EXISTS data_nascimento TIMESTAMP;