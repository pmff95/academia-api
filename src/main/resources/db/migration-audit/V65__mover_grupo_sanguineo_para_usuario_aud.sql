-- Move coluna grupo_sanguineo de aluno_aud para usuario_aud
ALTER TABLE usuario_aud
    ADD COLUMN grupo_sanguineo VARCHAR(20);

UPDATE usuario_aud ua
SET grupo_sanguineo = aa.grupo_sanguineo
FROM aluno_aud aa
WHERE ua.id = aa.id AND ua.rev = aa.rev;

ALTER TABLE aluno_aud
    DROP COLUMN grupo_sanguineo;
