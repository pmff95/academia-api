-- Remove a coluna nivel_ensino da tabela turma
ALTER TABLE turma DROP COLUMN nivel_ensino;

-- Adiciona a coluna nivel_ensino na tabela disciplina
ALTER TABLE disciplina
    ADD COLUMN nivel_ensino VARCHAR(50);

-- Adiciona constraint para garantir os valores do enum NivelEnsino
ALTER TABLE disciplina
    ADD CONSTRAINT chk_nivel_ensino_disciplina
        CHECK (nivel_ensino IN (
                                'MATERNAL',
                                'EDUCACAO_INFANTIL',
                                'FUNDAMENTAL_ANOS_INICIAIS',
                                'FUNDAMENTAL_ANOS_FINAIS',
                                'ENSINO_MEDIO'
            ));
