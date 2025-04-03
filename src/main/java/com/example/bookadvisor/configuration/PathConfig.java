package com.example.bookadvisor.configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PathConfig {

    @Bean
    public Path rootLocation() {
        return Paths.get("uploadDir");
    }
}
