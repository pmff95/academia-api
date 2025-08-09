-- Removendo coluna 'foto' da tabela 'aluno_aud'
ALTER TABLE aluno_aud
DROP COLUMN IF EXISTS foto;