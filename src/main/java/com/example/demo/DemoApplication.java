package com.example.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DemoApplication {

    static {
        String profile = System.getProperty("spring.profiles.active", "default");

        if ("dev".equals(profile) || "default".equals(profile)) {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();

            dotenv.entries().forEach(entry ->
                    System.setProperty(entry.getKey(), entry.getValue()));
        }
    }
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
