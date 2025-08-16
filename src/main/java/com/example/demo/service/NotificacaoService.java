package com.example.demo.service;

import com.example.demo.common.security.SecurityUtils;
import com.example.demo.common.security.UsuarioLogado;
import com.example.demo.domain.enums.TipoNotificacao;
import com.example.demo.dto.NotificacaoDTO;
import com.example.demo.entity.Aluno;
import com.example.demo.entity.Notificacao;
import com.example.demo.entity.Usuario;
import com.example.demo.exception.ApiException;
import com.example.demo.mapper.NotificacaoMapper;
import com.example.demo.repository.AlunoRepository;
import com.example.demo.repository.NotificacaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class NotificacaoService {
    private final NotificacaoRepository repository;
    private final EmailService emailService;
    private final NotificacaoMapper mapper;
    private final AlunoRepository alunoRepository;

    public NotificacaoService(NotificacaoRepository repository,
                              EmailService emailService,
                              NotificacaoMapper mapper,
                              AlunoRepository alunoRepository) {
        this.repository = repository;
        this.emailService = emailService;
        this.mapper = mapper;
        this.alunoRepository = alunoRepository;
    }

    public void criar(Usuario destinatario, String mensagem, TipoNotificacao tipo) {
        Notificacao n = new Notificacao();
        n.setDestinatario(destinatario);
        n.setMensagem(mensagem);
        n.setTipo(tipo);
        repository.save(n);
        dispatch(n);
    }

    private void dispatch(Notificacao notificacao) {
        if (notificacao.getTipo() == TipoNotificacao.EMAIL) {
            emailService.enviarNotificacao(notificacao.getDestinatario().getEmail(),
                    "Notificação", notificacao.getMensagem());
        } else if (notificacao.getTipo() == TipoNotificacao.PUSH) {
            // TODO: integrar com serviço de push notification
            System.out.println("Push: " + notificacao.getMensagem());
        }
    }

    public List<NotificacaoDTO> listarDoUsuarioLogado() {
        UsuarioLogado usuario = SecurityUtils.getUsuarioLogadoDetalhes();
        if (usuario == null) {
            return List.of();
        }
        return repository.findByDestinatarioUuidOrderByDataCriacaoDesc(usuario.getUuid())
                .stream().map(mapper::toDto).toList();
    }

    public void marcarComoLida(UUID uuid) {
        Notificacao n = repository.findById(uuid).orElseThrow(() -> new ApiException("Notificação não encontrada"));
        n.setLida(true);
        repository.save(n);
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void verificarExpiracaoMatriculas() {
        LocalDate hoje = LocalDate.now();
        for (Aluno aluno : alunoRepository.findAll()) {
            if (aluno.getDataMatricula() != null) {
                LocalDate expiracao = aluno.getDataMatricula().plusYears(1);
                if (expiracao.minusDays(7).isEqual(hoje)) {
                    criar(aluno, "Sua matrícula expira em 7 dias", TipoNotificacao.EMAIL);
                } else if (expiracao.isEqual(hoje)) {
                    criar(aluno, "Sua matrícula expirou", TipoNotificacao.EMAIL);
                }
            }
        }
    }

    public void notificarNovaFichaTreino(Aluno aluno) {
        if (aluno != null) {
            criar(aluno, "Uma nova ficha de treino foi atribuída", TipoNotificacao.PUSH);
        }
    }
}
