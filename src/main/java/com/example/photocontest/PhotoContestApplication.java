package com.example.photocontest;

import com.example.photocontest.services.EmailService;
import io.github.cdimascio.dotenv.Dotenv;
import com.example.photocontest.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication
public class PhotoContestApplication {

    public static void main(String[] args) {
        EnvConfig.init();
        SpringApplication.run(PhotoContestApplication.class, args);
    }

}
