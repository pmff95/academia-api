//package com.example.demo.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import software.amazon.awssdk.core.sync.RequestBody;
//import software.amazon.awssdk.services.s3.S3Client;
//import software.amazon.awssdk.services.s3.model.PutObjectRequest;
//
//import java.io.IOException;
//import java.util.UUID;
//
//@Service
//public class CloudflareService {
//    private final S3Client s3Client;
//
//    @Value("${cloudflare.r2.bucket}")
//    private String bucket;
//
//    @Value("${cloudflare.r2.public-domain}")
//    private String publicDomain;
//
//    public CloudflareService(S3Client s3Client) {
//        this.s3Client = s3Client;
//    }
//
//    public String upload(MultipartFile file) throws IOException {
//        String key = UUID.randomUUID() + "-" + file.getOriginalFilename();
//        PutObjectRequest request = PutObjectRequest.builder()
//                .bucket(bucket)
//                .key(key)
//                .contentType(file.getContentType())
//                .build();
//        s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
//        return publicDomain + "/" + key;
//    }
//}
//
