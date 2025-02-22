package com.epam.domain;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Trainee getTrainee() {
        return trainee;
    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public String getTrainingName() {
        return trainingName;
    }

    public void setTrainingName(String trainingName) {
        this.trainingName = trainingName;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public LocalDateTime getTrainingDateTime() {
        return trainingDateTime;
    }

    public void setTrainingDateTime(LocalDateTime trainingDateTime) {
        this.trainingDateTime = trainingDateTime;
    }

    public int getTrainingDurationInHours() {
        return trainingDurationInHours;
    }

    public void setTrainingDurationInHours(int trainingDurationInHours) {
        this.trainingDurationInHours = trainingDurationInHours;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Training training = (Training) o;
        return trainingDurationInHours == training.trainingDurationInHours && trainer.equals(training.trainer) && trainee.equals(training.trainee) && trainingName.equals(training.trainingName) && trainingType.equals(training.trainingType) && trainingDateTime.equals(training.trainingDateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(trainer, trainee, trainingName, trainingType, trainingDateTime, trainingDurationInHours);
    }

    @Override
    public String toString() {
        return "Training{" +
                "id=" + id +
                ", trainer=" + trainer +
                ", trainee=" + trainee +
                ", trainingName='" + trainingName + '\'' +
                ", trainingType=" + trainingType +
                ", trainingDateTime=" + trainingDateTime +
                ", trainingDuration=" + trainingDurationInHours +
                '}';
    }
}
