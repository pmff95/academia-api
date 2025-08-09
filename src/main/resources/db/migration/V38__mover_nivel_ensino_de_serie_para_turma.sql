-- Remove da tabela serie
ALTER TABLE serie DROP COLUMN nivel_ensino;

-- Adiciona na tabela turma
ALTER TABLE turma ADD COLUMN nivel_ensino VARCHAR(50);

-- Adiciona constraint para garantir os valores do enum
ALTER TABLE turma
    ADD CONSTRAINT chk_nivel_ensino
        CHECK (nivel_ensino IN (
                                'MATERNAL',
                                'EDUCACAO_INFANTIL',
                                'FUNDAMENTAL_ANOS_INICIAIS',
                                'FUNDAMENTAL_ANOS_FINAIS',
                                'ENSINO_MEDIO'
            ));
