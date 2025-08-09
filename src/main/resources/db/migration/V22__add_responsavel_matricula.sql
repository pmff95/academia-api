-- Remove a constraint atual
ALTER TABLE controle_matricula
DROP CONSTRAINT chk_tipo_matricula_valid;

-- Adiciona a nova constraint incluindo o tipo 'RESPONSAVEL'
ALTER TABLE controle_matricula
    ADD CONSTRAINT chk_tipo_matricula_valid
        CHECK (tipo IN ('ALUNO', 'PROFESSOR', 'FUNCIONARIO', 'RESPONSAVEL'));
