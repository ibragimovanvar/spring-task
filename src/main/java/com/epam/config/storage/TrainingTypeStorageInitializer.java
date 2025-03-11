package com.epam.config.storage;

import com.epam.dao.TrainingTypeDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.domain.TrainingType;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class TrainingTypeStorageInitializer {

    @Value("classpath:storage/training_type.csv") // Corrected property value
    private Resource trainingResource;

    @Autowired
    private TrainingTypeDao trainingTypeDao;


    @Order(1)
    @EventListener(ContextRefreshedEvent.class)
    public void initStorage() {
        List<TrainingType> trainingTypes;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(trainingResource.getInputStream()))) {
            trainingTypes = reader.lines()
                    .skip(1)
                    .map(this::parseTraining)
                    .toList();

            for(TrainingType trainingType : trainingTypes){
                trainingTypeDao.save(trainingType);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load trainees from CSV", e);
        }
    }

    private TrainingType parseTraining(String line) {
        String[] parts = line.split(",");
        return new TrainingType(Long.valueOf(parts[0].trim()), parts[1].trim());
    }
}
