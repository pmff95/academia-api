-- V86__insert_parametros_series.sql

INSERT INTO parametro (
    chave, descricao, valor_default, uuid,
    criado_em, atualizado_em, status, version, tipo,
    necessario_confirmacao, mensagem_confirmacao_ativo, mensagem_confirmacao_inativo,
    mensagem_confirmacao_alteracao, tipo_valor
) VALUES
      ('bercario_i_periodo', 'BERÇÁRIO I', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do berçário I?', 'TEXTO'),

      ('bercario_ii_periodo', 'BERÇÁRIO II', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do berçário II?', 'TEXTO'),

      ('maternal_i_periodo', 'MATERNAL I', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do maternal I?', 'TEXTO'),

      ('maternal_ii_periodo', 'MATERNAL II', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do maternal II?', 'TEXTO'),

      ('pre_i_periodo', 'PRÉ I', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do pré I?', 'TEXTO'),

      ('pre_ii_periodo', 'PRÉ II', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do pré II?', 'TEXTO'),

      ('primeiro_ano_periodo', '1º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 1º ano?', 'TEXTO'),

      ('segundo_ano_periodo', '2º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 2º ano?', 'TEXTO'),

      ('terceiro_ano_periodo', '3º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 3º ano?', 'TEXTO'),

      ('quarto_ano_periodo', '4º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 4º ano?', 'TEXTO'),

      ('quinto_ano_periodo', '5º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 5º ano?', 'TEXTO'),

      ('sexto_ano_periodo', '6º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 6º ano?', 'TEXTO'),

      ('setimo_ano_periodo', '7º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 7º ano?', 'TEXTO'),

      ('oitavo_ano_periodo', '8º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 8º ano?', 'TEXTO'),

      ('nono_ano_periodo', '9º ANO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 9º ano?', 'TEXTO'),

      ('primeiro_medio_periodo', '1º ANO MÉDIO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 1º ano do ensino médio?', 'TEXTO'),

      ('segundo_medio_periodo', '2º ANO MÉDIO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 2º ano do ensino médio?', 'TEXTO'),

      ('terceiro_medio_periodo', '3º ANO MÉDIO', 'BIMESTRE', gen_random_uuid(),
       now(), now(), 'ATIVO', 0, 'ACADEMICO', true, null, null,
       'Tem certeza que deseja alterar o período do 3º ano do ensino médio?', 'TEXTO');
