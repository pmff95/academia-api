package com.example.demo.common.config.file;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

import java.net.URI;

//@Configuration
//public class S3Config {
//
//    @Value("${cloudflare.r2.access-key}")
//    private String accessKey;
//
//    @Value("${cloudflare.r2.secret-key}")
//    private String secretKey;
//
//    @Value("${cloudflare.r2.endpoint}")
//    private String endpoint;
//
//    @Bean
//    public S3Client s3Client() {
//        return S3Client.builder()
//                .credentialsProvider(
//                        StaticCredentialsProvider.create(
//                                AwsBasicCredentials.create(accessKey, secretKey)
//                        )
//                )
//                .region(Region.of("us-east-1"))
//                .serviceConfiguration(S3Configuration.builder()
//                        .pathStyleAccessEnabled(true)
//                        .build())
//                .endpointOverride(URI.create(endpoint))
//                .build();
//    }
//
//}
