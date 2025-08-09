-- Adicionando coluna 'data_nascimento' na tabela 'usuario'
ALTER TABLE usuario
    ADD COLUMN IF NOT EXISTS data_nascimento TIMESTAMP;
