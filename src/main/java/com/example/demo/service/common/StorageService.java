package com.example.demo.service.common;

import com.example.demo.dto.common.StorageInput;
import com.example.demo.dto.common.StorageOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {


    private final S3Client s3Client;

    @Value("${cloudflare.r2.bucket}")
    private String bucketImagens;

    @Value("${cloudflare.r2.public-domain}")
    private String publicDomain;


    public StorageOutput uploadFile(StorageInput input) throws IOException {
        String ext = input.getFileName().contains(".")
                ? input.getFileName().substring(input.getFileName().lastIndexOf('.'))
                : ".jpg";

        String fileName = input.getPrefix() + UUID.randomUUID() + ext;

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketImagens)
                .key(fileName)
                .contentType(input.getMimeType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(input.getFileInputStream(), input.getFileSize()));

        String url = publicDomain + "/" + fileName;

        return new StorageOutput(fileName, url, input.getMimeType());

    }

    public StorageOutput uploadFile(MultipartFile file) {
        if (file == null || file.isEmpty())
            return null;

        try {
            StorageInput input = new StorageInput.Builder()
                    .withFileInputStream(file.getInputStream())
                    .withFileName(file.getOriginalFilename())
                    .withMimeType(file.getContentType())
                    .withFileSize(file.getSize())
                    .build();

            return uploadFile(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar arquivo", e);
        }
    }

    public void deleteFile(String key) {
        if (key == null || key.isBlank())
            return;
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucketImagens)
                    .key(key)
                    .build();
            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            System.err.println("Erro ao remover arquivo do storage: " + e.getMessage());
        }
    }

}
