package com.epam.config.storage;

import com.epam.domain.Trainer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Configuration
public class TrainerStorageInitializer {

    @Value("classpath:storage/trainer.csv") // Corrected property value
    private Resource trainerResource;

    @PostConstruct
    public List<Trainer> initStorage() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(trainerResource.getInputStream()))) {
           return reader.lines()
                    .skip(1)
                    .map(this::parseTrainer)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load trainees from CSV", e);
        }
    }

    private Trainer parseTrainer(String line) {
        String[] parts = line.split(",");
        return new Trainer(Long.valueOf(parts[0].trim()),parts[1].trim(), parts[2].trim(), Boolean.valueOf(parts[3].trim()), parts[4].trim());
    }
}
