package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.dto.CargaDTO;
import com.example.demo.dto.FichaTreinoCategoriaDTO;
import com.example.demo.dto.FichaTreinoDTO;
import com.example.demo.dto.FichaTreinoExercicioDTO;
import com.example.demo.entity.Exercicio;
import com.example.demo.entity.FichaTreino;
import com.example.demo.mapper.FichaTreinoMapper;
import com.example.demo.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FichaTreinoServiceTest {

    @Mock
    private FichaTreinoRepository fichaTreinoRepository;
    @Mock
    private AlunoRepository alunoRepository;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private ExercicioRepository exercicioRepository;
    @Mock
    private FichaTreinoHistoricoRepository historicoRepository;
    @Mock
    private NotificacaoService notificacaoService;
    @Mock
    private TreinoSessaoRepository treinoSessaoRepository;
    @Mock
    private TreinoDesempenhoRepository desempenhoRepository;
    @Mock
    private TreinoSessaoService treinoSessaoService;

    private final FichaTreinoMapper mapper = new FichaTreinoMapper(new ModelMapper());

    @Test
    void shouldSaveCategoriesWhenCreatingFicha() {
        FichaTreinoService service = new FichaTreinoService(
                fichaTreinoRepository,
                mapper,
                alunoRepository,
                professorRepository,
                usuarioRepository,
                exercicioRepository,
                historicoRepository,
                notificacaoService,
                treinoSessaoRepository,
                desempenhoRepository,
                treinoSessaoService
        );

        UUID exercicioUuid = UUID.randomUUID();
        Exercicio exercicio = new Exercicio();
        exercicio.setUuid(exercicioUuid);
        when(exercicioRepository.findById(exercicioUuid)).thenReturn(Optional.of(exercicio));
        when(fichaTreinoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CargaDTO carga = new CargaDTO();
        carga.setPeso(10.0);
        carga.setRepeticoes(10);

        FichaTreinoExercicioDTO exDto = new FichaTreinoExercicioDTO();
        exDto.setExercicioUuid(exercicioUuid);
        exDto.setTipo("FIXO");
        exDto.setSeries(3);
        exDto.setTempoDescanso(60);
        exDto.setCargas(List.of(carga));

        FichaTreinoCategoriaDTO catDto = new FichaTreinoCategoriaDTO();
        catDto.setNome("Treino A");
        catDto.setExercicios(List.of(exDto));

        FichaTreinoDTO dto = new FichaTreinoDTO();
        dto.setNome("Ficha");
        dto.setPreset(true);
        dto.setCategorias(List.of(catDto));

        try (MockedStatic<SecurityUtils> mocked = Mockito.mockStatic(SecurityUtils.class)) {
            mocked.when(SecurityUtils::getUsuarioLogadoDetalhes).thenReturn(null);
            service.save(dto);
        }

        ArgumentCaptor<FichaTreino> captor = ArgumentCaptor.forClass(FichaTreino.class);
        verify(fichaTreinoRepository).save(captor.capture());
        assertEquals(1, captor.getValue().getCategorias().size());
    }
}
