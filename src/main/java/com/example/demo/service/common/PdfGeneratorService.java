package com.example.demo.service.common;

import com.example.demo.dto.academico.plano.BoletimPdfDTO;
import com.example.demo.dto.academico.plano.NotaPeriodoPdfDTO;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfGeneratorService {

    public byte[] gerarPdf(String html, Map<String, Object> dados) throws IOException {
        MustacheFactory mf = new DefaultMustacheFactory();
        Mustache mustache = mf.compile(new StringReader(html), "tpl");
        StringWriter writer = new StringWriter();
        mustache.execute(writer, dados).flush();
        String processed = writer.toString();

        ByteArrayOutputStream pdfBaos = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(processed);
        renderer.layout();
        renderer.getSharedContext().setPrint(true);
        renderer.getSharedContext().setInteractive(false);
        renderer.createPDF(pdfBaos);
        return pdfBaos.toByteArray();
    }

    /**
     * Normalize an arbitrary HTML input into valid XHTML with UTF-8 charset.
     * This ensures the output is ready for further processing such as PDF generation.
     *
     * @param html raw HTML content
     * @return normalized XHTML string
     * @throws IllegalArgumentException if parsing fails or the HTML is invalid
     */
    public String normalizarHtml(String html) {
        try {
            Document doc = Jsoup.parse(html);
            doc.outputSettings()
                    .syntax(Document.OutputSettings.Syntax.xml)
                    .escapeMode(Entities.EscapeMode.xhtml)
                    .charset(StandardCharsets.UTF_8);

            if (doc.selectFirst("meta[charset]") == null) {
                doc.head().prependElement("meta").attr("charset", "UTF-8");
            }

            String xhtml = doc.html();
            if (!xhtml.contains("<html")) {
                throw new IllegalArgumentException("HTML inválido");
            }
            return xhtml;
        } catch (Exception e) {
            throw new IllegalArgumentException("HTML inválido", e);
        }
    }

    /**
     * Constrói o <code>Map<String, Object></code> que o template Mustache do boletim espera.
     *
     * @param escolaInfo   dados da escola (nome, cnpj, telefone, etc.)
     * @param alunoInfo    dados do aluno (nome, matrícula, série, etc.)
     * @param periodos     lista em ordem de períodos avaliativos (1º Bimestre, 2º Bimestre ...)
     * @param disciplinas  lista de disciplinas com suas notas por período
     * @param obsGeral     observação geral do boletim
     * @return mapa de dados pronto para o <code>gerarPdf</code>
     */
    public Map<String, Object> buildBoletimData(
            Map<String,Object> escolaInfo,
            Map<String,Object> alunoInfo,
            List<String> periodos,
            List<BoletimPdfDTO> disciplinas,
            String obsGeral) {

        Map<String,Object> root = new HashMap<>();
        root.putAll(escolaInfo);
        root.putAll(alunoInfo);
        root.put("OBSERVACAO_GERAL", obsGeral);

        String tblClass = calcularClasseTabela(periodos.size(), disciplinas.size());
        root.put("TABLE_CLASS", tblClass);         //  ←  usado lá no template

        /* cabeçalhos de período ------------------------------ */
        List<Map<String, String>> periodHeaders = periodos.stream()
                .map(p -> Map.of("PERIOD_NAME", p))
                .toList();
        root.put("PERIOD_HEADERS", periodHeaders);

        /* grade de notas -------------------------------------- */
        List<Map<String,Object>> discGrade = new ArrayList<>();
        List<Map<String,String>> discObs   = new ArrayList<>();

        for (BoletimPdfDTO d : disciplinas) {
            /* --- linha principal --- */
            Map<String,Object> discMap = new HashMap<>();
            discMap.put("DISC_NOME", d.nome);

            List<Map<String,Object>> periodVals = new ArrayList<>();
            for (String p : periodos) {
                NotaPeriodoPdfDTO np = d.notasPorPeriodo
                        .getOrDefault(p, new NotaPeriodoPdfDTO("-", "-", "-"));
                periodVals.add(Map.of(
                        "NOTA",   np.nota,
                        "FALTAS", np.faltas,
                        "FREQ",   np.freq
                ));
            }
            discMap.put("PERIOD_VALUES", periodVals);
            discMap.put("MEDIA_FINAL",    d.mediaFinal);
            discMap.put("FREQ_FINAL",     d.freqFinal);
            discMap.put("SITUACAO_FINAL", d.situacaoFinal);
            discGrade.add(discMap);

            /* --- observação (para nova seção) --- */
            if (d.observacao != null && !d.observacao.isBlank()) {
                discObs.add(Map.of("DISC", d.nome, "OBS", d.observacao));
            }
        }

        root.put("DISCIPLINAS", discGrade);

        /* nova chave para <Observações por Disciplina> -------- */
        if (!discObs.isEmpty()) {
            root.put("DISC_OBS", discObs);
        }

        return root;
    }


    /**
     * Gera um PDF de boletim com dados MOCK para testes rápidos.
     */
    public byte[] gerarPdfBoletimMock() throws IOException {
        // -------- Dados da escola ---------
        Map<String, Object> escola = Map.of(
                "SCHOOL_NAME", "Escola Exemplo",
                "SCHOOL_CNPJ", "12.345.678/0001-90",
                "SCHOOL_PHONE", "(11) 99999-9999",
                "SCHOOL_ADDRESS", "Rua das Flores, 123 - Centro",
                "SCHOOL_LOGO_URL", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2OakGxXL8Ky49O1z3kqsU0MZK2MB_EDDieg&s"
        );

        // -------- Dados do aluno ---------
        Map<String, Object> aluno = Map.of(
                "STUDENT_NAME", "João da Silva",
                "STUDENT_MATRICULA", "202501234",
                "STUDENT_SERIE", "1º Ano EM",
                "STUDENT_TURMA", "A",
                "STUDENT_TURNO", "Manhã",
                "STUDENT_ITINERARIO", "Humanas",
                "STUDENT_PHOTO_URL", "https://cdn-icons-png.flaticon.com/512/3413/3413565.png",
                "ANO_LETIVO", LocalDate.now().getYear()
        );

        // -------- Períodos ---------
        List<String> periodos = List.of("1º Bim", "2º Bim", "3º Bim", "4º Bim");

        // -------- Disciplinas com notas ---------
        List<BoletimPdfDTO> discList = new ArrayList<>();
        BoletimPdfDTO mat = new BoletimPdfDTO("Matemática");
        mat.addNota("1º Bim", new NotaPeriodoPdfDTO("8,5", "2", "95%"));
        mat.addNota("2º Bim", new NotaPeriodoPdfDTO("7,8", "1", "97%"));
        mat.addNota("3º Bim", new NotaPeriodoPdfDTO("9,0", "0", "100%"));
        mat.addNota("4º Bim", new NotaPeriodoPdfDTO("8,9", "0", "99%"));
        mat.mediaFinal = "8,55";
        mat.freqFinal = "98%";
        mat.situacaoFinal = "Aprovado";
        mat.observacao = null;
        discList.add(mat);

        BoletimPdfDTO hist = new BoletimPdfDTO("História");
        hist.addNota("1º Bim", new NotaPeriodoPdfDTO("6,5", "3", "90%"));
        hist.addNota("2º Bim", new NotaPeriodoPdfDTO("7,0", "2", "92%"));
        hist.addNota("3º Bim", new NotaPeriodoPdfDTO("7,5", "1", "95%"));
        hist.addNota("4º Bim", new NotaPeriodoPdfDTO("8,0", "0", "97%"));
        hist.mediaFinal = "7,25";
        hist.freqFinal = "94%";
        hist.situacaoFinal = "Aprovado";
        hist.observacao = "Melhorou bastante ao longo do ano.";
        discList.add(hist);

        String obsGeral = "Parabéns pelo esforço e dedicação ao longo do ano letivo!";

        Map<String, Object> dados = buildBoletimData(escola, aluno, periodos, discList, obsGeral);
        String htmlTemplate = carregarTemplate("boletim/boletim.html", true);
        return gerarPdf(htmlTemplate, dados);
    }

    public String carregarTemplate(String classpath, boolean normalize) throws IOException {
        // Ex.: classpath = "boletim/boletim.html"
        Resource res = new ClassPathResource(classpath);
        byte[] bytes = res.getInputStream().readAllBytes();
        String html = new String(bytes, StandardCharsets.UTF_8);

        return normalize ? normalizarHtml(html) : html;          // garante XHTML válido
    }

    /** Define a classe da tabela conforme nº de colunas e nº de linhas. */
    private String calcularClasseTabela(int qtdPeriodos, int qtdDisciplinas) {
        int colunas = 1 + (qtdPeriodos * 3) + 3;

        if (colunas > 22 || qtdDisciplinas > 25) return "xs";
        if (colunas > 16 || qtdDisciplinas > 18) return "sm";
        return "";
    }
}
