INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
-- 🔹 Supinos com Barra
(gen_random_uuid(), 'Supino Reto com Barra', 'Deite-se em um banco plano, segure a barra na largura dos ombros e desça controladamente até o peito, empurrando de volta até a extensão completa.', 'PEITO'),
(gen_random_uuid(), 'Supino Inclinado com Barra', 'Banco inclinado a 30-45º, segure a barra e empurre acima do peitoral superior.', 'PEITO'),
(gen_random_uuid(), 'Supino Declinado com Barra', 'Banco declinado, segure a barra e faça o movimento focando na parte inferior do peitoral.', 'PEITO'),

-- 🔹 Supinos com Halteres
(gen_random_uuid(), 'Supino Reto com Halteres', 'Deite-se em banco plano, segure halteres e desça até o peitoral, subindo em arco.', 'PEITO'),
(gen_random_uuid(), 'Supino Inclinado com Halteres', 'Banco a 45º, segure halteres e empurre acima do peitoral superior.', 'PEITO'),
(gen_random_uuid(), 'Supino Declinado com Halteres', 'Banco declinado, use halteres para focar no peitoral inferior.', 'PEITO'),

-- 🔹 Crucifixos
(gen_random_uuid(), 'Crucifixo Reto com Halteres', 'Banco plano, braços semiflexionados e abertos, aproxime os halteres em arco sobre o peitoral.', 'PEITO'),
(gen_random_uuid(), 'Crucifixo Inclinado com Halteres', 'Banco inclinado, braços semiflexionados e abertos, foco no peitoral superior.', 'PEITO'),
(gen_random_uuid(), 'Crucifixo Declinado com Halteres', 'Banco declinado, braços abertos, movimento de arco focando no peitoral inferior.', 'PEITO'),
(gen_random_uuid(), 'Crucifixo na Máquina (Peck-Deck)', 'Sente-se na máquina peck-deck e aproxime os braços até a frente do peito.', 'PEITO'),

-- 🔹 Crossovers (Cabos)
(gen_random_uuid(), 'Cross-over Alto no Cabo', 'Cabos ajustados acima da cabeça, puxe para baixo em arco até frente da cintura.', 'PEITO'),
(gen_random_uuid(), 'Cross-over Médio no Cabo', 'Cabos na altura dos ombros, puxe até juntar mãos à frente do peito.', 'PEITO'),
(gen_random_uuid(), 'Cross-over Baixo no Cabo', 'Cabos fixados abaixo da cintura, puxe para cima em arco até frente do peito.', 'PEITO'),

-- 🔹 Flexões
(gen_random_uuid(), 'Flexão de Braço Tradicional', 'Apoie as mãos na largura dos ombros, desça controladamente até próximo do chão e suba estendendo os braços.', 'PEITO'),
(gen_random_uuid(), 'Flexão com Mãos Afastadas', 'Mãos mais abertas que os ombros, aumenta ativação do peitoral.', 'PEITO'),
(gen_random_uuid(), 'Flexão com Pés Elevados', 'Pés sobre banco/step, maior foco no peitoral superior.', 'PEITO'),
(gen_random_uuid(), 'Flexão Diamante', 'Mãos juntas em forma de diamante, foco em tríceps e peitoral interno.', 'PEITO'),
(gen_random_uuid(), 'Flexão Explosiva (Plyo Push-up)', 'Empurre explosivamente para tirar as mãos do chão, desenvolvendo força e potência no peitoral.', 'PEITO'),

-- 🔹 Mergulhos
(gen_random_uuid(), 'Mergulho em Paralelas (Peito)', 'Incline o tronco à frente nas paralelas, descendo até alongar o peitoral.', 'PEITO'),

-- 🔹 Exercícios com Máquina
(gen_random_uuid(), 'Supino na Máquina Smith (Reto)', 'Realize supino no banco reto utilizando a barra da máquina Smith.', 'PEITO'),
(gen_random_uuid(), 'Supino Inclinado na Máquina Smith', 'Banco inclinado, supino com barra da máquina Smith para maior estabilidade.', 'PEITO'),
(gen_random_uuid(), 'Supino Declinado na Máquina Smith', 'Banco declinado, execute supino guiado na máquina Smith.', 'PEITO'),
(gen_random_uuid(), 'Pressão Horizontal no Aparelho', 'Sente-se na máquina de chest press e empurre as alavancas até a extensão completa.', 'PEITO'),

-- 🔹 Outros
(gen_random_uuid(), 'Pullover com Halteres', 'Deite-se em banco, segure o halter com ambas as mãos e leve de trás da cabeça até sobre o peito.', 'PEITO'),
(gen_random_uuid(), 'Isométrico de Peito (Palmas Juntas)', 'Una as palmas das mãos na frente do peito e pressione, mantendo a contração.', 'PEITO');

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
-- 🔹 Puxadas Verticais
(gen_random_uuid(), 'Puxada Aberta na Barra Fixa', 'Segure a barra fixa com pegada aberta e puxe o corpo até o queixo ultrapassar a barra.', 'COSTAS'),
(gen_random_uuid(), 'Puxada Fechada na Barra Fixa (pegada supinada)', 'Pegada supinada e próxima, puxe até o queixo ultrapassar a barra, foco em dorsais inferiores e bíceps.', 'COSTAS'),
(gen_random_uuid(), 'Puxada na Frente (Pulldown)', 'Na polia alta, puxe a barra até o peito com pegada pronada aberta.', 'COSTAS'),
(gen_random_uuid(), 'Puxada Atrás da Nuca (Pulldown)', 'Na polia alta, puxe a barra atrás da cabeça, mantendo postura ereta.', 'COSTAS'),
(gen_random_uuid(), 'Puxada com Triângulo (Close Grip Pulldown)', 'Na polia alta, use a barra em V e puxe até o peito.', 'COSTAS'),

-- 🔹 Remadas
(gen_random_uuid(), 'Remada Curvada com Barra', 'Segure a barra pronada, incline o tronco e puxe a barra até o abdômen.', 'COSTAS'),
(gen_random_uuid(), 'Remada Curvada com Halteres', 'Segure dois halteres, incline o tronco e puxe até a lateral do abdômen.', 'COSTAS'),
(gen_random_uuid(), 'Remada Unilateral com Halter', 'Apoie um joelho no banco e puxe o halter com uma das mãos até o quadril.', 'COSTAS'),
(gen_random_uuid(), 'Remada Cavalinho (T-Bar Row)', 'Segure a barra com suporte em T e puxe até o abdômen.', 'COSTAS'),
(gen_random_uuid(), 'Remada na Máquina Sentado (Low Row)', 'Sente-se, segure a barra com pegada neutra e puxe até a cintura.', 'COSTAS'),
(gen_random_uuid(), 'Remada Alta na Polia', 'Na polia baixa, puxe a barra em direção ao peito mantendo cotovelos altos.', 'COSTAS'),

-- 🔹 Levantamentos
(gen_random_uuid(), 'Levantamento Terra (Deadlift)', 'Segure a barra no chão com pegada pronada e levante mantendo costas retas até ficar em pé.', 'COSTAS'),
(gen_random_uuid(), 'Levantamento Terra Romeno', 'Com barra ou halteres, desça mantendo pernas semi-flexionadas e lombar alinhada.', 'COSTAS'),
(gen_random_uuid(), 'Levantamento Terra com Pegada Sumo', 'Pernas afastadas, barra próxima ao corpo, levante mantendo postura ereta.', 'COSTAS'),

-- 🔹 Encolhimentos / Trapézio
(gen_random_uuid(), 'Encolhimento de Ombros com Barra', 'Segure a barra em frente ao corpo e eleve os ombros até o máximo.', 'COSTAS'),
(gen_random_uuid(), 'Encolhimento de Ombros com Halteres', 'Segure halteres ao lado do corpo e eleve os ombros verticalmente.', 'COSTAS'),

-- 🔹 Lombar / Core
(gen_random_uuid(), 'Extensão Lombar no Banco Romano', 'Deite no banco romano, flexione o tronco e retorne até a posição neutra.', 'COSTAS'),
(gen_random_uuid(), 'Good Morning com Barra', 'Com barra nas costas, flexione o tronco à frente mantendo a lombar reta.', 'COSTAS'),
(gen_random_uuid(), 'Hiperextensão no Solo', 'De bruços, eleve o tronco contraindo a lombar.', 'COSTAS'),

-- 🔹 Isoladores / Cabos
(gen_random_uuid(), 'Pull-Over na Polia Alta com Barra', 'Na polia alta, puxe a barra em arco até frente da coxa, mantendo cotovelos semiflexionados.', 'COSTAS'),
(gen_random_uuid(), 'Pull-Over com Corda', 'Na polia alta, segure a corda e puxe até o quadril com amplitude total.', 'COSTAS'),
(gen_random_uuid(), 'Face Pull', 'Na polia alta com corda, puxe em direção ao rosto abrindo os cotovelos.', 'COSTAS'),

-- 🔹 Peso Corporal
(gen_random_uuid(), 'Barra Fixa Pronada', 'Pegada aberta pronada, puxe até o queixo ultrapassar a barra.', 'COSTAS'),
(gen_random_uuid(), 'Barra Fixa Supinada', 'Pegada supinada mais fechada, foco em dorsais e bíceps.', 'COSTAS');

--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------
--------------------------------------------------------------------------

INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
-- 🔹 Testa (Skull Crusher / French Press)
(gen_random_uuid(), 'Tríceps Testa com Barra W', 'Deite-se no banco, segure a barra W e flexione os cotovelos levando a barra próxima à testa, depois estenda.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Testa com Halteres', 'Deite-se no banco, segure halteres de forma neutra e flexione os cotovelos até próximo à testa.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Francês (French Press) em Pé', 'Em pé, segure a barra W acima da cabeça e flexione os cotovelos até atrás da nuca.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Francês com Halter Unilateral', 'Segure um halter acima da cabeça com uma mão e flexione atrás da nuca.', 'TRICEPS'),

-- 🔹 Corda / Polia
(gen_random_uuid(), 'Tríceps Corda na Polia Alta', 'Na polia alta, segure a corda e estenda os cotovelos abrindo no final do movimento.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Barra Reta na Polia Alta', 'Na polia alta, use a barra reta e estenda até baixo da coxa.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Barra V na Polia Alta', 'Na polia alta, use a barra em V e estenda controladamente.', 'TRICEPS'),
(gen_random_uuid(), 'Kickback com Halteres', 'Com o tronco inclinado, segure halteres e estenda os braços para trás.', 'TRICEPS'),

-- 🔹 Peso corporal
(gen_random_uuid(), 'Mergulho em Paralelas (Tríceps)', 'Nas barras paralelas, tronco mais ereto, desça até 90º e suba contraindo tríceps.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps no Banco (Bench Dips)', 'Com as mãos apoiadas no banco atrás do corpo, flexione os cotovelos descendo até 90º e suba.', 'TRICEPS'),
(gen_random_uuid(), 'Flexão Diamante', 'Apoie as mãos próximas em formato de diamante e realize a flexão.', 'TRICEPS'),

-- 🔹 Máquinas
(gen_random_uuid(), 'Tríceps Máquina Extensão', 'Sente-se na máquina de tríceps e empurre a alavanca para baixo até extensão total.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Coice no Cabo (Kickback)', 'Na polia baixa, segure a alça e estenda o braço para trás.', 'TRICEPS'),

-- 🔹 Variações barra
(gen_random_uuid(), 'Tríceps Testa com Barra Reta', 'Deitado no banco, use barra reta e flexione até próximo da testa.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Press no Smith', 'Na máquina Smith, desça a barra até a testa e estenda.', 'TRICEPS'),

-- 🔹 Close Grip (Press Pegada Fechada)
(gen_random_uuid(), 'Supino Pegada Fechada', 'No banco reto, use pegada fechada para maior ativação dos tríceps.', 'TRICEPS'),
(gen_random_uuid(), 'Flexão Pegada Fechada', 'Mãos próximas no solo, flexão focada em tríceps.', 'TRICEPS'),

-- 🔹 Outros
(gen_random_uuid(), 'Tríceps Overhead com Corda', 'Na polia alta, puxando a corda atrás da cabeça, estenda os braços para cima.', 'TRICEPS'),
(gen_random_uuid(), 'Tríceps Isométrico', 'Mantenha os braços em extensão contra resistência, segurando a contração.', 'TRICEPS');
-- ========================================
-- BÍCEPS
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Rosca Direta com Barra Reta', 'Segure a barra na largura dos ombros e flexione os cotovelos até a contração máxima.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca Direta com Barra W', 'Similar à rosca direta, porém com barra W para reduzir estresse no punho.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca Alternada com Halteres', 'Com halteres, flexione alternadamente supinando o punho.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca Martelo', 'Segure halteres em pegada neutra e flexione mantendo punho firme.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca Concentrada', 'Sentado, apoie o braço na coxa e flexione isolando o bíceps.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca Scott (Banco)', 'Apoie os braços no banco Scott e faça a flexão controlada.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca Inversa com Barra', 'Pegada pronada, flexione até a contração máxima.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca no Cabo', 'Na polia baixa, puxe a barra em direção ao ombro.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca 21', '7 repetições em meio movimento inferior, 7 superiores e 7 completas.', 'BICEPS'),
                                                           (gen_random_uuid(), 'Rosca no TRX', 'Com o corpo inclinado, puxe o peso do corpo usando os bíceps.', 'BICEPS');

-- ========================================
-- OMBRO
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Desenvolvimento Militar com Barra', 'Pressione a barra acima da cabeça a partir da altura dos ombros.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Desenvolvimento com Halteres', 'Sentado ou em pé, empurre halteres acima da cabeça.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Elevação Lateral com Halteres', 'Com braços semiflexionados, eleve halteres até a linha dos ombros.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Elevação Frontal com Halteres', 'Levante halteres à frente até a altura dos ombros.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Elevação Posterior (Fly Inverso)', 'Incline o tronco e eleve halteres para os lados, foco no deltoide posterior.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Arnold Press', 'Inicie com halteres na frente do corpo e gire ao elevar acima da cabeça.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Remada Alta com Barra', 'Segure barra pronada e puxe até altura do queixo, cotovelos altos.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Desenvolvimento na Máquina Smith', 'Empurre a barra guiada para cima com segurança.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Elevação Lateral no Cabo', 'Na polia baixa, eleve lateralmente focando na contração.', 'OMBRO'),
                                                           (gen_random_uuid(), 'Face Pull', 'Na polia alta, puxe a corda em direção ao rosto, foco no deltoide posterior.', 'OMBRO');

-- ========================================
-- ANTEBRAÇO
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Rosca de Punho com Barra', 'Sentado, antebraços apoiados, flexione apenas os punhos.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Extensão de Punho com Barra', 'Movimento inverso da rosca de punho, elevando o dorso da mão.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Rosca de Punho com Halteres', 'Mesmo movimento, porém utilizando halteres.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Farmer Walk', 'Segure halteres pesados e caminhe mantendo postura ereta.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Pegada no Barra Fixa Isométrica', 'Segure a barra fixa e mantenha a contração máxima pelo tempo possível.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Pronação e Supinação com Halter', 'Gire o punho para cima e para baixo controladamente.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Toalha na Barra Fixa', 'Segure toalhas na barra fixa para aumentar a força de pegada.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Rosca Inversa', 'Com barra pronada, flexione cotovelos ativando antebraço.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Pinch Grip Plate Hold', 'Segure anilhas pelos dedos em pinça e mantenha isometria.', 'ANTEBRACO'),
                                                           (gen_random_uuid(), 'Dead Hang', 'Fique pendurado na barra fixa o máximo possível.', 'ANTEBRACO');

-- ========================================
-- TRAPÉZIO
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Encolhimento de Ombros com Barra', 'Segure a barra à frente e eleve os ombros até o máximo.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Encolhimento de Ombros com Halteres', 'Segure halteres ao lado do corpo e eleve os ombros verticalmente.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Encolhimento na Máquina Smith', 'Barra guiada, realize encolhimento mantendo postura ereta.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Remada Alta com Barra', 'Barra pronada, puxe até o queixo, ativando trapézio superior.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Remada Alta com Halteres', 'Mesma execução, usando halteres.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Face Pull', 'Na polia alta, puxe corda em direção ao rosto, ativando trapézio médio.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Shrug Isométrico', 'Mantenha a posição de encolhimento dos ombros o máximo possível.', 'TRAPEZIO'),
                                                           (gen_random_uuid(), 'Barra Fixa Pegada Pronada', 'Além das costas, ativa bastante o trapézio médio/inferior.', 'TRAPEZIO');

-- ========================================
-- LOMBAR
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Extensão Lombar no Banco Romano', 'Deite no banco, flexione o tronco e volte até posição neutra.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Hiperextensão no Solo', 'De bruços, eleve o tronco contraindo a lombar.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Good Morning com Barra', 'Barra nas costas, incline o tronco à frente mantendo lombar reta.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Levantamento Terra Convencional', 'Barra no chão, levante até posição ereta mantendo lombar firme.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Levantamento Terra Romeno', 'Segure barra ou halteres, desça até meia canela mantendo lombar alinhada.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Levantamento Terra Sumo', 'Pernas afastadas, segure a barra e levante com lombar estabilizada.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Superman', 'De bruços, eleve braços e pernas simultaneamente contraindo lombar.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Prancha Isométrica com Elevação', 'Mantenha prancha e eleve alternadamente uma perna para ativar lombar.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Extensão Lombar na Bola Suíça', 'Deite de bruços na bola suíça e eleve o tronco.', 'LOMBAR'),
                                                           (gen_random_uuid(), 'Kettlebell Swing', 'Movimento balístico que ativa quadril, glúteo e lombar.', 'LOMBAR');
-- ========================================
-- ABDÔMEN
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Abdominal Reto no Solo', 'Deite de costas e eleve o tronco em direção aos joelhos.', 'ABDOMEN'),
                                                           (gen_random_uuid(), 'Abdominal Infra', 'Deite-se e eleve as pernas até 90° focando no abdômen inferior.', 'ABDOMEN'),
                                                           (gen_random_uuid(), 'Abdominal Bicicleta', 'Alternar cotovelo ao joelho oposto em movimento de pedalada.', 'ABDOMEN'),
                                                           (gen_random_uuid(), 'Abdominal Canivete', 'Deite e eleve simultaneamente pernas e tronco, tocando mãos nos pés.', 'ABDOMEN'),
                                                           (gen_random_uuid(), 'Prancha Frontal', 'Apoie antebraços e pés no chão, mantendo o corpo alinhado.', 'ABDOMEN'),
                                                           (gen_random_uuid(), 'Prancha Dinâmica', 'Alterne apoio entre antebraços e mãos, mantendo abdômen contraído.', 'ABDOMEN');

-- ========================================
-- OBLÍQUOS
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Abdominal Oblíquo no Solo', 'Deite-se e leve o cotovelo ao joelho oposto.', 'OBLIQUO'),
                                                           (gen_random_uuid(), 'Prancha Lateral', 'Deite de lado, apoie o antebraço e eleve o quadril, mantendo alinhamento.', 'OBLIQUO'),
                                                           (gen_random_uuid(), 'Torção Russa (Russian Twist)', 'Sentado, incline levemente o tronco e gire de um lado para o outro.', 'OBLIQUO'),
                                                           (gen_random_uuid(), 'Elevação de Pernas com Rotação', 'Deitado, eleve pernas e gire quadril de um lado ao outro.', 'OBLIQUO'),
                                                           (gen_random_uuid(), 'Cabo Woodchopper Alto para Baixo', 'Na polia alta, puxe o cabo em diagonal de cima para baixo.', 'OBLIQUO'),
                                                           (gen_random_uuid(), 'Cabo Woodchopper Baixo para Cima', 'Na polia baixa, puxe o cabo em diagonal de baixo para cima.', 'OBLIQUO');

-- ========================================
-- CORE (Estabilização)
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Prancha Frontal Isométrica', 'Apoie antebraços no chão e mantenha corpo alinhado.', 'CORE'),
                                                           (gen_random_uuid(), 'Prancha Lateral Isométrica', 'Apoie um braço lateralmente e mantenha corpo alinhado.', 'CORE'),
                                                           (gen_random_uuid(), 'Ab Wheel Rollout', 'Com roda de abdominais, estenda o corpo para frente e retorne.', 'CORE'),
                                                           (gen_random_uuid(), 'Dead Bug', 'Deitado, eleve pernas e braços e alterne descendo lado oposto.', 'CORE'),
                                                           (gen_random_uuid(), 'Bird Dog', 'De quatro apoios, estenda braço e perna opostos simultaneamente.', 'CORE'),
                                                           (gen_random_uuid(), 'Farmer Walk', 'Caminhe segurando cargas pesadas, mantendo core estabilizado.', 'CORE');

-- ========================================
-- GLÚTEOS
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Agachamento Livre', 'Flexione quadris e joelhos até 90º mantendo tronco ereto.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Afundo (Lunge)', 'Dê um passo à frente e flexione o joelho até 90º.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Avanço Caminhando', 'Execução do afundo em deslocamento.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Glute Bridge', 'Deitado, eleve o quadril contraindo glúteos.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Hip Thrust com Barra', 'Apoie costas no banco, barra sobre quadril e eleve.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Stiff com Barra', 'Flexione quadris mantendo pernas semiflexionadas, contraindo glúteos e posteriores.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Abdução de Quadril na Máquina', 'Sentado, empurre as almofadas para fora.', 'GLUTEOS'),
                                                           (gen_random_uuid(), 'Abdução de Quadril com Faixa', 'Com faixa elástica nos joelhos, abra as pernas lateralmente.', 'GLUTEOS');

-- ========================================
-- QUADRÍCEPS
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Agachamento Livre com Barra', 'Flexione quadris e joelhos até 90º mantendo barra nos ombros.', 'QUADRICEPS'),
                                                           (gen_random_uuid(), 'Agachamento Frontal', 'Barra à frente sobre ombros, flexione até 90º.', 'QUADRICEPS'),
                                                           (gen_random_uuid(), 'Leg Press 45°', 'Empurre a plataforma flexionando joelhos até 90º.', 'QUADRICEPS'),
                                                           (gen_random_uuid(), 'Cadeira Extensora', 'Sentado, estenda os joelhos contra resistência.', 'QUADRICEPS'),
                                                           (gen_random_uuid(), 'Hack Machine', 'Na máquina hack, flexione e estenda joelhos com barra guiada.', 'QUADRICEPS'),
                                                           (gen_random_uuid(), 'Step-up no Banco', 'Suba em banco segurando carga adicional.', 'QUADRICEPS');

-- ========================================
-- POSTERIORES DE COXA
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Mesa Flexora', 'Sentado ou deitado, flexione os joelhos contra resistência.', 'POSTERIOR'),
                                                           (gen_random_uuid(), 'Stiff com Barra', 'Flexione quadris mantendo pernas semiflexionadas, foco nos posteriores.', 'POSTERIOR'),
                                                           (gen_random_uuid(), 'Levantamento Terra Romeno', 'Desça com barra até meia canela, contraindo posteriores.', 'POSTERIOR'),
                                                           (gen_random_uuid(), 'Glute Ham Raise', 'Deitado no banco GHR, flexione os joelhos contra resistência.', 'POSTERIOR'),
                                                           (gen_random_uuid(), 'Good Morning', 'Flexione quadris com barra apoiada nas costas.', 'POSTERIOR'),
                                                           (gen_random_uuid(), 'Swing com Kettlebell', 'Movimento explosivo de quadril ativando posteriores.', 'POSTERIOR');

-- ========================================
-- PANTURRILHAS
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Elevação de Panturrilha em Pé', 'Fique em pé e eleve os calcanhares ao máximo.', 'PANTURRILHA'),
                                                           (gen_random_uuid(), 'Elevação de Panturrilha Sentado', 'Sentado, com carga sobre coxas, eleve os calcanhares.', 'PANTURRILHA'),
                                                           (gen_random_uuid(), 'Elevação de Panturrilha no Leg Press', 'Na máquina leg press, posicione ponta dos pés e empurre.', 'PANTURRILHA'),
                                                           (gen_random_uuid(), 'Elevação de Panturrilha Unilateral', 'Apoie-se em um pé só e faça o movimento completo.', 'PANTURRILHA'),
                                                           (gen_random_uuid(), 'Pular Corda', 'Movimento cíclico de impulsão que ativa panturrilhas.', 'PANTURRILHA');

-- ========================================
-- ADUTORES
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Cadeira Adutora', 'Sentado, una as pernas contra resistência.', 'ADUTOR'),
                                                           (gen_random_uuid(), 'Adutor com Faixa Elástica', 'Coloque faixa nos tornozelos e puxe a perna em direção à outra.', 'ADUTOR'),
                                                           (gen_random_uuid(), 'Sumo Deadlift', 'Pernas bem afastadas, barra próxima ao corpo, foco em adutores.', 'ADUTOR'),
                                                           (gen_random_uuid(), 'Agachamento Sumo', 'Agachamento com pernas afastadas e pés voltados para fora.', 'ADUTOR');

-- ========================================
-- ABDUTORES
-- ========================================
INSERT INTO exercicio (uuid, nome, descricao, musculo) VALUES
                                                           (gen_random_uuid(), 'Cadeira Abdutora', 'Sentado, afaste as pernas contra resistência.', 'ABDUTOR'),
                                                           (gen_random_uuid(), 'Abdução de Quadril deitado', 'Deitado de lado, eleve a perna até 45°.', 'ABDUTOR'),
                                                           (gen_random_uuid(), 'Abdução com Faixa Elástica', 'Com faixa elástica nos joelhos, afaste as pernas lateralmente.', 'ABDUTOR'),
                                                           (gen_random_uuid(), 'Abdução no Cabo', 'Na polia baixa, prenda caneleira e afaste a perna lateralmente.', 'ABDUTOR');
