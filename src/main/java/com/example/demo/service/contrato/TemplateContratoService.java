package com.example.demo.service.contrato;

import com.example.demo.common.response.exception.EurekaException;
import com.example.demo.domain.model.contrato.TemplateContrato;
import com.example.demo.repository.contrato.TemplateContratoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.dto.contrato.TemplateResumoDTO;
import com.example.demo.mapper.TemplateResumoMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demo.service.common.PdfGeneratorService;

@Service
@RequiredArgsConstructor
public class TemplateContratoService {

    private final TemplateContratoRepository repository;
    private final S3Client s3Client;
    private final PdfGeneratorService pdfGeneratorService;

    @Value("${cloudflare.r2.bucket}")
    private String bucket;

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([A-Z0-9_]+)\\}}");

    public void salvarTemplate(MultipartFile file, String nome) throws IOException {
        String html = new String(file.getBytes(), StandardCharsets.UTF_8);
        String xhtml;
        try {
            xhtml = pdfGeneratorService.normalizarHtml(html);
        } catch (IllegalArgumentException e) {
            throw EurekaException.ofValidation("HTML inválido");
        }

        if (Pattern.compile("\\{\\{([A-Z0-9_]+)\\}\\}").matcher(xhtml).results().count() == 0) {
            throw EurekaException.ofValidation("Template sem placeholders.");
        }

        Set<String> placeholders = new LinkedHashSet<>();
        Matcher m = PLACEHOLDER_PATTERN.matcher(xhtml);
        while (m.find()) {
            placeholders.add(m.group(1));
        }

        String uuid = UUID.randomUUID().toString();
        String key = "contratos/" + uuid + ".html";

        PutObjectRequest put = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("text/html")
                .build();
        s3Client.putObject(put, RequestBody.fromBytes(xhtml.getBytes(StandardCharsets.UTF_8)));

        TemplateContrato template = new TemplateContrato();
        template.setNome(nome);
        template.setKey(key);
        template.setPlaceholders(String.join(",", placeholders));

        repository.save(template);
    }

    public byte[] gerarPdf(UUID uuid, Map<String, Object> dados) throws IOException {
        TemplateContrato template = repository.findByUuid(uuid)
                .orElseThrow(() -> EurekaException.ofNotFound("Template não encontrado."));

        Set<String> requeridos = new HashSet<>(Arrays.asList(template.getPlaceholders().split(",")));
        if (!dados.keySet().containsAll(requeridos)) {
            throw EurekaException.ofValidation("Dados do contrato incompletos.");
        }

        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(bucket)
                .key(template.getKey())
                .build();
        try (InputStream in = s3Client.getObject(get)) {
            String original = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            return pdfGeneratorService.gerarPdf(original, dados);
        }
    }

    public Page<TemplateResumoDTO> findAll(String nome, Pageable pageable) {
        String search = nome == null ? "" : nome;
        Page<TemplateContrato> page = repository.findByNomeContainingIgnoreCase(search, pageable);
        return page.map(TemplateResumoMapper::toDto);
    }
}
