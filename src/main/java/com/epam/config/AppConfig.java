package com.epam.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = "com.epam")
public class AppConfig {
    @Bean
    @Profile("dev")
    public String logConfigDev() {
        System.setProperty("logging.config", "classpath:log4j2-dev.xml");
        return "dev";
    }

    @Bean
    @Profile("prod")
    public String logConfigProd() {
        System.setProperty("logging.config", "classpath:log4j2-prod.xml");
        return "prod";
    }
}