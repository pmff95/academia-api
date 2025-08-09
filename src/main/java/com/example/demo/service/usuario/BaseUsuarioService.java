package com.example.demo.service.usuario;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.enums.MetodoAutenticacao;
import com.example.demo.domain.enums.Perfil;
import com.example.demo.domain.enums.Status;
import com.example.demo.domain.enums.Sexo;
import com.example.demo.domain.model.instituicao.Escola;
import com.example.demo.domain.model.usuario.Usuario;
import com.example.demo.repository.usuario.UsuarioRepository;
import com.example.demo.service.common.PasswordService;
import com.example.demo.service.controle.ControleMatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

/**
 * Classe auxiliar para construir instâncias de {@link Usuario}
 * ou de suas subclasses com as validações padronizadas.
 */
@Service
@RequiredArgsConstructor
public class BaseUsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordService passwordService;
    private final ControleMatriculaService controleMatriculaService;

    /**
     * Preenche um objeto de usuário com dados comuns após realizar
     * as validações de CPF e e‑mail.
     *
     * @param usuario         instância de usuário (ou subclasse) a ser preenchida
     * @param escola          escola vinculada
     * @param perfil          perfil do usuário
     * @param nome            nome do usuário
     * @param email           e‑mail do usuário
     * @param cpf             cpf do usuário
     * @param telefone        telefone do usuário
     * @param fotoUrl         url da foto
     * @param matriculaManual matrícula manual, caso exista
     * @param senha           senha em texto plano
     * @return usuário preenchido
     */
    public <T extends Usuario> T construirUsuario(T usuario,
                                                  Escola escola,
                                                  Perfil perfil,
                                                  String nome,
                                                  String mae,
                                                  String pai,
                                                  String email,
                                                  String cpf,
                                                  LocalDate dataNascimento,
                                                  Sexo sexo,
                                                  String telefone,
                                                  String fotoUrl,
                                                  String matriculaManual,
                                                  String senha) {

        String trimmedEmail = email != null ? email.trim() : null;
        if (trimmedEmail != null && !trimmedEmail.isBlank() &&
                usuarioRepository.existsByEmailAndEscolaUuid(trimmedEmail, escola.getUuid())) {
            throw EurekaException.ofValidation("O email " + trimmedEmail + " já está cadastrado para esta escola");
        }

        String cleanedCpf = cpf != null ? cpf.trim().replaceAll("\\D", "") : null;
        if (cleanedCpf != null && !cleanedCpf.isBlank() &&
                usuarioRepository.existsByCpfAndEscolaUuid(cleanedCpf, escola.getUuid())) {
            throw EurekaException.ofValidation("O CPF " + cleanedCpf + " já está cadastrado para esta escola");
        }

        String cleanedTelefone = telefone != null ? telefone.trim().replaceAll("\\D", "") : null;
        if (cleanedTelefone != null && !cleanedTelefone.isBlank() &&
                usuarioRepository.existsByTelefone(cleanedTelefone)) {
            throw EurekaException.ofValidation(cleanedTelefone + " já está cadastrado");
        }

        if(perfil == Perfil.PROFESSOR){
            usuario.setPai(pai);
            usuario.setMae(mae);
        }

        usuario.setEscola(escola);
        usuario.setNome(nome);
        usuario.setEmail(trimmedEmail);
        usuario.setPerfil(perfil);
        usuario.setMetodoAutenticacao(MetodoAutenticacao.SENHA);
        usuario.setCpf(cleanedCpf);
        LocalDate nascimento = dataNascimento != null ? dataNascimento : LocalDate.of(1990, 1, 1);
        usuario.setDataNascimento(nascimento);
        usuario.setSexo(sexo);
        usuario.setMatricula(controleMatriculaService.gerarNovaMatricula(perfil, escola));
        if (matriculaManual != null && !matriculaManual.isBlank()) {
            usuario.setMatriculaManual(matriculaManual);
        }
        usuario.setStatus(Status.ATIVO);
        usuario.setFoto(fotoUrl);
        usuario.setTelefone(cleanedTelefone);
        usuario.setPrimeiroAcesso(true);
        usuario.setSenha(passwordService.validateAndEncode(senha));
        return usuario;
    }
}
