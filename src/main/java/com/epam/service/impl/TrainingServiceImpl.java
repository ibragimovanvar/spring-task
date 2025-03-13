package com.epam.service.impl;

import com.epam.dao.TraineeDao;
import com.epam.dao.TrainerDao;
import com.epam.dao.TrainingDao;
import com.epam.domain.Trainee;
import com.epam.domain.Trainer;
import com.epam.domain.Training;
import com.epam.service.TrainingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service("trainingService")
public class TrainingServiceImpl implements TrainingService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TrainingServiceImpl.class);
    private static final String ENTITY_NAME = "Training";

    private final TrainingDao trainingDao;
    private final TraineeDao traineeDao;
    private final TrainerDao trainerDao;

    @Autowired
    public TrainingServiceImpl(TrainingDao trainingDao, TraineeDao traineeDao, TrainerDao trainerDao) {
        LOGGER.info("{}Service Bean initialized", ENTITY_NAME);
        this.trainingDao = trainingDao;
        this.traineeDao = traineeDao;
        this.trainerDao = trainerDao;
    }

    @Override
    public List<Training> getTraineeTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                              String trainerName, String trainingType) {
        LOGGER.info("Request to get trainings for trainee with username: {}", username);
        return traineeDao.findTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);
    }

    @Override
    public List<Training> getTrainerTrainings(String username, LocalDateTime fromDate, LocalDateTime toDate,
                                              String traineeName) {
        LOGGER.info("Request to get trainings for trainer with username: {}", username);
        return trainerDao.findTrainerTrainings(username, fromDate, toDate, traineeName);
    }

    @Transactional
    @Override
    public void addTraining(Training training) {
        LOGGER.info("Request to add training: {}", training);

        validateRequiredFields(training);

        training = trainingDao.save(training);

        Trainee trainee = training.getTrainee();
        Trainer trainer = training.getTrainer();

        if (!trainee.getTrainings().contains(training)) {
            trainee.getTrainings().add(training);
            traineeDao.update(trainee);
        }

        if (!trainer.getTrainings().contains(training)) {
            trainer.getTrainings().add(training);
            trainerDao.update(trainer);
        }

        LOGGER.info("Training added successfully: {}", training.getId());
    }

    private void validateRequiredFields(Training training) {
        if (training.getTrainer() == null ||
                training.getTrainee() == null ||
                training.getTrainingName() == null ||
                training.getTrainingDateTime() == null ||
                training.getTrainingDurationInHours() == null) {
            throw new IllegalArgumentException("Required training fields are missing");
        }
    }

    private void requireAuthentication(String username, String role) {
        boolean isAuthenticated = false;
        if ("trainee".equals(role)) {
            isAuthenticated = traineeDao.findByUsername(username).isPresent();
        } else if ("trainer".equals(role)) {
            isAuthenticated = trainerDao.findByUsername(username).isPresent();
        }

        if (!isAuthenticated) {
            throw new SecurityException("Authentication required for " + role + " with username: " + username);
        }
    }
}