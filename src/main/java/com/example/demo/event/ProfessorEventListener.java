package com.example.demo.event;

import com.example.demo.domain.model.carteira.Cartao;
import com.example.demo.service.carteira.CarteiraService;
import com.example.demo.service.usuario.UsuarioEmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProfessorEventListener {

    private final CarteiraService carteiraService;
    private final UsuarioEmailService usuarioEmailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onProfessorCreated(ProfessorCreatedEvent event) {
        Cartao novoCartao = null;
        String senhaCartao = event.getSenhaCartao();
        if (event.getNumeroCartao() != null) {
            novoCartao = carteiraService.criarCarteiraParaProfessor(
                    event.getProfessor(),
                    event.getNumeroCartao(),
                    senhaCartao
            );
        }
        List<String> emailsCartao = List.of();
        String numeroCartao = null;
        if (novoCartao != null) {
            numeroCartao = novoCartao.getNumero();
            emailsCartao = event.getProfessor().getEmail() == null || event.getProfessor().getEmail().isBlank()
                    ? List.of()
                    : List.of(event.getProfessor().getEmail());
        }
        String infoLogin = usuarioEmailService.montarInfoLogin(event.getProfessor());
        usuarioEmailService.enviarEmailBoasVindas(
                event.getProfessor().getNome(),
                event.getProfessor().getEmail(),
                event.getSenhaUsuario(),
                infoLogin,
                emailsCartao,
                numeroCartao,
                senhaCartao
        );
    }
}
