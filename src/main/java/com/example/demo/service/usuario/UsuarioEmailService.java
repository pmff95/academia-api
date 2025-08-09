package com.example.demo.service.usuario;

import com.example.demo.dto.email.EmailDto;
import com.example.demo.dto.email.EmailHtml;
import com.example.demo.service.common.EmailService;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.model.usuario.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioEmailService {

    private final EmailService emailService;

    public String montarInfoLogin(Usuario usuario) {
        StringBuilder sb = new StringBuilder("Opções de login:\n");
        if (usuario.getEmail() != null && !usuario.getEmail().isBlank()) {
            sb.append("- Email: ").append(usuario.getEmail()).append("\n");
        }
        if (usuario.getMatricula() != null && !usuario.getMatricula().isBlank()) {
            sb.append("- Matrícula: ").append(usuario.getMatricula()).append("\n");
        }
        if (usuario.getMatriculaManual() != null && !usuario.getMatriculaManual().isBlank()) {
            sb.append("- Matrícula manual: ").append(usuario.getMatriculaManual()).append("\n");
        }
        if (usuario.getNickname() != null && !usuario.getNickname().isBlank()) {
            sb.append("- Nickname: ").append(usuario.getNickname()).append("\n");
        }
        if (usuario.getCpf() != null && !usuario.getCpf().isBlank()) {
            sb.append("- CPF: ").append(usuario.getCpf()).append("\n");
        }
        if (usuario.getTelefone() != null && !usuario.getTelefone().isBlank()) {
            sb.append("- Telefone: ").append(usuario.getTelefone()).append("\n");
        }

//        sb.append("Perfis disponíveis: ")
//                .append(java.util.Arrays.stream(Perfil.values())
//                        .map(Enum::name)
//                        .toList())
//                .append("\n");
        return sb.toString();
    }

    public void enviarEmailNovoUsuario(String nome, String email, String senha, String infoLogin) {
        if (email == null || email.isBlank()) return;

        String html = String.format("""
                    <html>
                    <body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">
                        <div style=\"max-width: 600px; margin: auto; background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); text-align: center;\">
                            <img src=\"https://img.freepik.com/vetores-gratis/ilustracao-do-conceito-de-login_114360-739.jpg\"
                                 alt=\"Login\"
                                 style=\"width: 30%%; display: block; margin: 0 auto 20px auto;\">
                            <h2>Olá, %s!</h2>
                            <p>Seu cadastro foi realizado com sucesso.</p>
                            <p><strong>Senha temporária:</strong></p>
                            <p style=\"font-size: 18px; font-weight: bold; color: #2c3e50; background: #ecf0f1; padding: 10px; border-radius: 4px; margin: 20px auto; display: inline-block;\">
                                %s
                            </p>
                            <p>Use qualquer uma das opções abaixo para fazer login:</p>
                            <pre style=\"text-align: left;\">%s</pre>
                            <p style=\"color: #888; font-size: 12px; margin-top: 30px;\">
                                Recomendamos alterar a senha após o primeiro acesso.<br>Equipe do Sistema
                            </p>
                        </div>
                    </body>
                    </html>
                """, nome, senha, infoLogin);

        emailService.sendEmailAsync(
                new EmailDto(
                        null,
                        List.of(email),
                        List.of(),
                        List.of(),
                        "Suas credenciais chegaram!",
                        List.of(),
                        new EmailHtml(html, null)
                )
        );
    }

    public void enviarEmailRecuperacaoSenha(String nome, String email, String senha) {
        if (email == null || email.isBlank()) return;

        String html = String.format("""
                    <html>
                    <body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">
                        <div style=\"max-width: 600px; margin: auto; background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); text-align: center;\">
                            <img src=\"https://img.freepik.com/vetores-gratis/ilustracao-do-conceito-de-login_114360-739.jpg\"
                                 alt=\"Login\"
                                 style=\"width: 30%%; display: block; margin: 0 auto 20px auto;\">
                            <h2>Olá, %s!</h2>
                            <p>Recebemos uma solicitação para redefinir sua senha.</p>
                            <p><strong>Seu token temporário é:</strong></p>
                            <p style=\"font-size: 18px; font-weight: bold; color: #2c3e50; background: #ecf0f1; padding: 10px; border-radius: 4px; margin: 20px auto; display: inline-block;\">
                                %s
                            </p>
                            <p>Recomendamos que você altere essa senha após o primeiro acesso.</p>
                            <p style=\"color: #888; font-size: 12px; margin-top:30px;\">
                                Se você não solicitou essa alteração, ignore este e-mail.<br>Equipe do Sistema
                            </p>
                        </div>
                    </body>
                    </html>
                """, nome, senha);

        emailService.sendEmailAsync(
                new EmailDto(
                        null,
                        List.of(email),
                        List.of(),
                        List.of(),
                        "Senha redefinida!",
                        List.of(),
                        new EmailHtml(html, null)
                )
        );
    }

    public void enviarEmailNovoCartao(String nome, List<String> emails, String numero, String senha) {
        List<String> validEmails = emails == null ? List.of() : emails.stream()
                .filter(e -> e != null && !e.isBlank())
                .toList();
        if (validEmails.isEmpty()) return;

        String body = String.format("Olá, %s! A senha do seu cartão (%s) é:%n%s", nome, numero, senha);
        emailService.sendEmailAsync(
                new EmailDto(
                        body,
                        validEmails,
                        List.of(),
                        List.of(),
                        "Seu cartão foi cadastrado!",
                        List.of(),
                        null
                )
        );
    }

    public void enviarEmailBoasVindas(String nome,
                                      String emailUsuario,
                                      String senhaUsuario,
                                      String infoLogin,
                                      List<String> emailsCartao,
                                      String numeroCartao,
                                      String senhaCartao) {
        enviarEmailNovoUsuario(nome, emailUsuario, senhaUsuario, infoLogin);
        if (numeroCartao != null && senhaCartao != null) {
            enviarEmailNovoCartao(nome, emailsCartao, numeroCartao, senhaCartao);
        }
    }
}
