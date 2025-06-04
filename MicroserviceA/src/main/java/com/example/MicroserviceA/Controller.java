package com.example.MicroserviceA;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
@RestController
public class Controller {

    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${logging.level.root}")
    private String loggingLevel;

    @GetMapping("/config")
    public Map<String, String> getConfig() {
        return Map.of(
                "server.port", serverPort,
                "spring.profiles.active", activeProfile,
                "spring.datasource.url", dataSourceUrl,
                "logging.level.root", loggingLevel
        );
    }
}

