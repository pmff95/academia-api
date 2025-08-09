-- Move coluna grupo_sanguineo de aluno para usuario
ALTER TABLE usuario
    ADD COLUMN grupo_sanguineo VARCHAR(20);

UPDATE usuario u
SET grupo_sanguineo = a.grupo_sanguineo
FROM aluno a
WHERE u.id = a.id;

ALTER TABLE aluno
    DROP COLUMN grupo_sanguineo;
