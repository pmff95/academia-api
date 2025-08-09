package com.example.demo.common.util;

import com.example.demo.domain.enums.TipoSenha;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utilitário para geração e validação de senhas de acordo com a política {@link TipoSenha}.
 * <p>
 * As três políticas disponíveis são:
 * <ul>
 *   <li><b>PADRÃO</b> – mínimo de 8 caracteres contendo ao menos 1 letra maiúscula, 1 letra minúscula,
 *       1 dígito numérico e 1 símbolo.</li>
 *   <li><b>NUMÉRICA</b> – somente dígitos de 0&nbsp;a&nbsp;9.</li>
 *   <li><b>ABERTA</b> – nenhuma restrição de composição.</li>
 * </ul>
 * Todos os métodos são <i>stateless</i> (thread‑safe) e a classe não pode ser instanciada.
 * </p>
 */
public class SenhaUtil {

    private static final Pattern PADRAO_REGEX =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)"
                    + "(?=.*[!@#$%^&*()\\-_=+\\[\\]{}|;:,.<>?]).{8,}$");
    private static final Pattern NUMERICA_REGEX = Pattern.compile("^\\d+$");

    /**
     * Impede instanciação.
     */
    private SenhaUtil() {
    }

    /**
     * Gera uma senha temporária compatível com o {@link TipoSenha} informado.
     * <p>
     * Para <b>PADRÃO</b> retorna 8 caracteres; para <b>NUMÉRICA</b>, 6 dígitos;
     * para <b>ABERTA</b>, 8 caracteres alfanuméricos. Ajuste os tamanhos se
     * necessário.
     * </p>
     *
     * @param tipo política a ser obedecida
     * @return senha temporária gerada de forma criptograficamente segura
     */
    public static String gerarSenhaTemporaria(TipoSenha tipo) {
        return switch (tipo) {
            case PADRAO -> gerarSenhaPadrao(8);
            case NUMERICA -> gerarSenhaNumerica(6);   // 6 dígitos - ajuste se quiser
            case ABERTA -> gerarSenhaAberta(8);
        };
    }

    /**
     * Gera um <em>PIN</em> numérico de 4 dígitos (0‑9), podendo conter zeros à
     * esquerda. Útil para autenticação rápida ou códigos de uso único.
     *
     * <pre>{@code
     * String pin = SenhaUtil.gerarSenhaTemporariaPin(); // ex.: "0742"
     * }</pre>
     *
     * @return {@code String} com exatamente 4 dígitos aleatórios
     */
    public static String gerarSenhaTemporariaPin() {
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {
            senha.append(random.nextInt(10)); // de 0 a 9
        }

        return senha.toString();
    }

    /**
     * Valida a {@code senha} de acordo com o {@link TipoSenha} especificado.
     *
     * @param senha texto a ser verificado
     * @param tipo  política de validação
     * @return {@code true} se a senha atende à política; caso contrário {@code false}
     */
    public static boolean validar(String senha, TipoSenha tipo) {
        if (senha == null)
            return false;

        return switch (tipo) {
            case PADRAO -> PADRAO_REGEX.matcher(senha).matches();
            case NUMERICA -> NUMERICA_REGEX.matcher(senha).matches();
            case ABERTA -> true; // qualquer coisa vale
        };
    }

    /**
     * Gera senha para o tipo <b>PADRÃO</b> - Mínimo de 8 caracteres, contendo: 1 letra maiúscula, 1 letra minúscula, 1 número e 1 símbolo.
     */
    private static String gerarSenhaPadrao(int tamanho) {
        String maiusculas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String minusculas = "abcdefghijklmnopqrstuvwxyz";
        String numeros = "0123456789";
        String especiais = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String todos = maiusculas + minusculas + numeros + especiais;

        SecureRandom r = new SecureRandom();
        List<Character> chars = new java.util.ArrayList<>(tamanho);

        chars.add(maiusculas.charAt(r.nextInt(maiusculas.length())));
        chars.add(minusculas.charAt(r.nextInt(minusculas.length())));
        chars.add(numeros.charAt(r.nextInt(numeros.length())));
        chars.add(especiais.charAt(r.nextInt(especiais.length())));

        for (int i = 4; i < tamanho; i++)
            chars.add(todos.charAt(r.nextInt(todos.length())));

        Collections.shuffle(chars, r);
        StringBuilder sb = new StringBuilder();
        chars.forEach(sb::append);
        return sb.toString();
    }

    /**
     * Gera senha para o tipo <b>NUMERICA</b> - Apenas dígitos (0-9).
     */
    /**
     * Gera uma senha numérica com o número de dígitos informado.
     *
     * @param tamanho quantidade de dígitos desejada
     * @return sequência numérica gerada
     */
    public static String gerarSenhaNumerica(int tamanho) {
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++)
            sb.append(r.nextInt(10));
        return sb.toString();
    }

    /**
     * Gera senha para o tipo <b>ABERTA</b> - Sem restrições de composição.
     */
    private static String gerarSenhaAberta(int tamanho) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom r = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tamanho; i++)
            sb.append(chars.charAt(r.nextInt(chars.length())));
        return sb.toString();
    }

}
