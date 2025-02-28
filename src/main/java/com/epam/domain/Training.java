package com.epam.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Training {
    private Long id;
    private Trainer trainer;
    private Trainee trainee;
    private String trainingName;
    private TrainingType trainingType;
    private LocalDateTime trainingDateTime;
    private Integer trainingDurationInHours;

    public Training() {
    }

    public Training(Long id) {
        this.id = id;
    }

    public Training(Long id, Trainer trainer, Trainee trainee, String trainingName, TrainingType trainingType, LocalDateTime trainingDateTime, Integer trainingDurationInHours) {
        this.id = id;
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDateTime = trainingDateTime;
        this.trainingDurationInHours = trainingDurationInHours;
    }

    public Training(Trainer trainer, Trainee trainee, String trainingName, TrainingType trainingType, LocalDateTime trainingDateTime, int trainingDurationInHours) {
        this.trainer = trainer;
        this.trainee = trainee;
        this.trainingName = trainingName;
        this.trainingType = trainingType;
        this.trainingDateTime = trainingDateTime;
        this.trainingDurationInHours = trainingDurationInHours;
    }
}
