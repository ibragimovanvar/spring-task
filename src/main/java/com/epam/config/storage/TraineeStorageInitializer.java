package com.epam.config.storage;

import com.epam.domain.Trainee;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class TraineeStorageInitializer {

    @Value("classpath:storage/trainee.csv") // Corrected property value
    private Resource traineeResource;

    @PostConstruct
    public List<Trainee> initStorage() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(traineeResource.getInputStream()))) {
           return reader.lines()
                    .skip(1)
                    .map(this::parseTrainee)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load trainees from CSV", e);
        }
    }

    private Trainee parseTrainee(String line) {
        String[] parts = line.split(",");
        return new Trainee(Long.valueOf(parts[0].trim()),parts[1].trim(), parts[2].trim(), Boolean.valueOf(parts[3].trim()), LocalDate.parse(parts[4].trim()), parts[5].trim());
    }
}
