package com.epam.service.facade;

import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.service.TraineeService;
import com.epam.service.TrainerService;
import com.epam.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("trainingFacade")
public class TrainingFacade {

    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainingService trainingService;

    @Autowired
    public TrainingFacade(TraineeService traineeService, TrainerService trainerService, TrainingService trainingService) {
        this.traineeService = traineeService;
        this.trainerService = trainerService;
        this.trainingService = trainingService;
    }

    public Trainer createTrainerProfile(Trainer trainer) {
        return trainerService.createTrainer(trainer);
    }

    public Trainee createTraineeProfile(Trainee trainee) {
        return traineeService.createTrainee(trainee);
    }

    public Training createTraining(Training training) {
        return trainingService.createTraining(training);
    }

    public Trainer updateTrainerProfile(Trainer trainer) {
        return trainerService.updateTrainer(trainer);
    }

    public Trainee updateTraineeProfile(Trainee trainee) {
        return traineeService.updateTrainee(trainee);
    }

    public Training updateTraining(Training training) {
        return trainingService.updateTraining(training);
    }

    public void deleteTrainerProfile(Long id) {
        trainerService.deleteTrainer(id);
    }

    public void deleteTraineeProfile(Long id) {
        traineeService.deleteTrainee(id);
    }

    public void deleteTraining(Long id) {
        trainingService.deleteTraining(id);
    }
}