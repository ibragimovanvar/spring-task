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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TrainingStorageInitializer {

    @Value("classpath:storage/training.csv") // Corrected property value
    private Resource trainingResource;

    @PostConstruct
    public List<Training> initStorage() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(trainingResource.getInputStream()))) {
           return reader.lines()
                    .skip(1)
                    .map(this::parseTraining)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load trainees from CSV", e);
        }
    }

    private Training parseTraining(String line) {
        String[] parts = line.split(",");
        return new Training(Long.valueOf(parts[0].trim()),new Trainer(Long.valueOf(parts[1].trim())), new Trainee(Long.valueOf(parts[2].trim())), parts[3].trim(), new TrainingType(parts[4].trim()), LocalDateTime.parse(parts[5].trim()), Integer.valueOf(parts[6].trim()));
    }
}
