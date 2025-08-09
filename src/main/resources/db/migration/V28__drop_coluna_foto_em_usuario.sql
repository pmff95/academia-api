-- Removendo coluna 'foto' da tabela 'aluno'
ALTER TABLE aluno
DROP COLUMN IF EXISTS foto;