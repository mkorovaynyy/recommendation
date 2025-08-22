package org.skypro.recommendation.config;

import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class BuildInfoConfig {

    @Bean
    public BuildProperties buildProperties() {
        Properties properties = new Properties();
        properties.setProperty("group", "org.skypro");
        properties.setProperty("artifact", "recommendation");
        properties.setProperty("version", "1.0.0");
        properties.setProperty("name", "recommendation-service");
        return new BuildProperties(properties);
    }
}