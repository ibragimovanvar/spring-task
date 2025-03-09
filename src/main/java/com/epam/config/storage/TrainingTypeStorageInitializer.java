package com.epam.config.storage;

import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

@Configuration
public class TrainingTypeStorageInitializer {

    @Value("classpath:storage/training_type.csv") // Corrected property value
    private Resource trainingResource;

    public List<TrainingType> initStorage() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(trainingResource.getInputStream()))) {
            return reader.lines()
                    .skip(1)
                    .map(this::parseTraining)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load trainees from CSV", e);
        }
    }

    private TrainingType parseTraining(String line) {
        String[] parts = line.split(",");
        return new TrainingType(Long.valueOf(parts[0].trim()), parts[1].trim());
    }
}
