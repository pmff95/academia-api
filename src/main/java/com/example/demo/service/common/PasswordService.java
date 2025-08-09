package com.example.demo.service.common;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.common.util.SenhaUtil;
import com.example.demo.domain.enums.TipoSenha;
import com.example.demo.dto.parametro.SimplesParametroValorView;
import com.example.demo.service.parametro.ParametroService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final ParametroService parametroService;

    public String generateTemporaryPassword() {
        return SenhaUtil.gerarSenhaTemporaria(getTipoSenhaParam());
    }

    public String generateTemporaryPin() {
        return SenhaUtil.gerarSenhaTemporariaPin();
    }

    /**
     * Gera uma senha numérica aleatória com a quantidade de dígitos especificada.
     *
     * @param digits número de dígitos desejado
     * @return sequência numérica gerada
     */
    public String generateNumericPassword(int digits) {
        return SenhaUtil.gerarSenhaNumerica(digits);
    }

    public String validateAndEncode(String rawPassword) {
        TipoSenha tipo = getTipoSenhaParam();
//        if (!SenhaUtil.validar(rawPassword, tipo)) {
//            throw EurekaException.ofValidation(
//                    "Senha não atende aos requisitos (" + requisitos(tipo) + ")");
//        }
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    private TipoSenha getTipoSenhaParam() {
        SimplesParametroValorView view = parametroService.retornaValorParametro("tipo_senha", false);
        String raw = (view == null) ? null : view.valor();

        if (raw == null || raw.isBlank()) {
            return TipoSenha.PADRAO;
        }

        try {
            return TipoSenha.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return TipoSenha.PADRAO;
        }
    }

    private String requisitos(TipoSenha tipo) {
        return switch (tipo) {
            case PADRAO  -> "Mínimo de 8 caracteres, contendo: 1 letra maiúscula, 1 letra minúscula, 1 número e 1 símbolo.";
            case NUMERICA -> "Apenas dígitos (0-9).";
            case ABERTA  -> "Sem restrições de composição.";
        };
    }
}
